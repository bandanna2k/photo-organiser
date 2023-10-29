package photo.organiser.optimisation;

import photo.organiser.ui.PhotoFrame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        new Main().start();
    }

    private void start() throws InterruptedException
    {
        List<Thread> threads = new ArrayList<>();
        {
            Thread thread = new Thread(() ->
            {
                try
                {
                    File tempFile = createTempFile();
                    File srcImage = new File("/tmp/images/1.jpg");
                    ImageOptimisation optimisation = new ImageIoOptimisation(srcImage, tempFile, 0.3f);
                    optimisation.optimise();

                    new PhotoFrame(srcImage, tempFile, optimisation.getClass().getSimpleName());
                    System.out.println("Finished 1");
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
            threads.add(thread);
        }
        {
            Thread thread = new Thread(() ->
            {
                try
                {
                    File tempFile = createTempFile();
                    File srcImage = new File("/tmp/images/1.jpg");
                    ImageOptimisation optimisation = new ThumbnailatorOptimisation(srcImage, tempFile, 0.3f);
                    optimisation.optimise();

                    new PhotoFrame(srcImage, tempFile, optimisation.getClass().getSimpleName());
                    System.out.println("Finished 1");
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
            threads.add(thread);
        }
        {
            Thread thread = new Thread(() ->
            {
                try
                {
                    File tempFile = createTempFile();
                    File srcImage = new File("/tmp/images/1.jpg");
                    ImageOptimisation optimisation = new JPEGOptimizerOptimisation(srcImage, tempFile, 0.3f);
                    optimisation.optimise();

                    new PhotoFrame(srcImage, tempFile, optimisation.getClass().getSimpleName());
                    System.out.println("Finished 1");
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
            threads.add(thread);
        }

        for (Thread t : threads)
        {
            t.join();
        }
    }

    private static File createTempFile() throws IOException
    {
        File tempFile = Files.createTempFile(ImageIoOptimisation.class.getSimpleName(), ".jpg").toFile();
        tempFile.deleteOnExit();
        return tempFile;
    }
}
