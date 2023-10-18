package photo.organiser;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class HashFinder
{
    enum Status
    {
        Waiting,
        FilenameCollecting,
        HashCollecting,
        Complete,
    }

    private Status status = Status.Waiting;

    private final Set<String> files = new TreeSet<>();
    private final Path dir;

    public HashFinder(Path dir)
    {
        this.dir = dir;
    }

    public void start()
    {
        try
        {
            status = Status.FilenameCollecting;
            Files.walkFileTree(dir, new FilenameCollector(files));

            // TODO Collect hashes

            status = Status.Complete;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public StatusRecord getStatus()
    {
        switch (status)
        {
            case Waiting:
            default:
                return new StatusRecord(0, 0, Status.Waiting);
            case FilenameCollecting:
                return new StatusRecord(0, 0, Status.FilenameCollecting);
            case HashCollecting:
                return new StatusRecord(files.size(), 0, Status.HashCollecting);
            case Complete:
                return new StatusRecord(files.size(), -1, Status.Complete);
        }
    }

    public static class StatusRecord
    {
        public final int totalFiles;
        public final int totalHashes;
        public final Status status;

        public StatusRecord(int totalFiles, int totalHashes, Status status)
        {
            this.totalFiles = totalFiles;
            this.totalHashes = totalHashes;
            this.status = status;
        }

        @Override
        public String toString()
        {
            return "StatusRecord{" +
                    "totalFiles=" + totalFiles +
                    ", totalHashes=" + totalHashes +
                    ", status=" + status +
                    '}';
        }
    }

    private static class FilenameCollector extends SimpleFileVisitor<Path>
    {
        private final Set<String> filenames;

        public FilenameCollector(Set<String> hashes)
        {
            this.filenames = hashes;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException
        {
            filenames.add(path.toFile().getAbsolutePath());
            return super.visitFile(path, attrs);
        }
    }
}
