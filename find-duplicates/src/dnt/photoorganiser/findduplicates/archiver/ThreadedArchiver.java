package dnt.photoorganiser.findduplicates.archiver;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadedArchiver implements Archiver, Closeable
{
    private final Thread thread;
    private final RunnableArchiver archiver;

    public ThreadedArchiver(Archiver archiver)
    {
        this.archiver = new RunnableArchiver(archiver);
        thread = new Thread(this.archiver);
        thread.start();
    }

    @Override
    public void close() throws IOException
    {
        try
        {
            archiver.close();
            thread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Path filePath)
    {
        archiver.add(filePath);
    }

    private static class RunnableArchiver implements Archiver, Runnable
    {
        final Archiver archiver;
        volatile boolean pleaseClose = false;

        final Queue<Path> paths = new ConcurrentLinkedQueue<>();

        private RunnableArchiver(Archiver archiver)
        {
            this.archiver = archiver;
        }

        @Override
        public void add(Path filePath)
        {
            paths.add(filePath);
        }

        @Override
        public void run()
        {
            try
            {
                while (!pleaseClose)
                {
                    addPaths();
                    Thread.sleep(10);
                }
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws IOException
        {
            addPaths();
            archiver.close();
            pleaseClose = true;
        }

        private void addPaths()
        {
            while(!paths.isEmpty())
            {
                Path poll = paths.poll();
                archiver.add(poll);
            }
        }
    }
}
