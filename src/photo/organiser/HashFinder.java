package photo.organiser;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private final MessageDigest md5;

    private final Path dir;
    private Status status = Status.Waiting;

    private final Set<File> files = new TreeSet<File>();

    private int countOfHashesCollected = 0;
    private final Map<String, List<File>> hashToFiles = new HashMap<>();

    public HashFinder(Path dir)
    {
        this.dir = dir;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void start()
    {
        try
        {
            status = Status.FilenameCollecting;
            Files.walkFileTree(dir, new FilenameCollector(files));

            status = Status.HashCollecting;
            try
            {
                for (File file : files)
                {
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    byte[] hash = md5.digest(bytes);
                    String base64 = Base64.getMimeEncoder().encodeToString(hash);

                    List<File> files = hashToFiles.get(base64);
                    if (null == files)
                    {
                        files = new ArrayList<>();
                        files.add(file);
                        hashToFiles.put(base64, files);
                    }
                    else
                    {
                        files.add(file);
                    }
                    countOfHashesCollected++;
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

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
                return new StatusRecord(0, 0, 0, Status.Waiting);
            case FilenameCollecting:
                return new StatusRecord(0, 0, 0, Status.FilenameCollecting);
            case HashCollecting:
                return new StatusRecord(files.size(), countOfHashesCollected, hashToFiles.size(), Status.HashCollecting);
            case Complete:
                return new StatusRecord(files.size(), countOfHashesCollected, hashToFiles.size(), Status.Complete);
        }
    }

    public static class StatusRecord
    {
        public final int countOfFiles;
        public final int countOfHashesCollected;
        public final int countOfUniqueHashes;
        public final Status status;

        public StatusRecord(int countOfFiles, int countOfHashesCollected, int countOfUniqueHashes, Status status)
        {
            this.countOfFiles = countOfFiles;
            this.countOfHashesCollected = countOfHashesCollected;
            this.countOfUniqueHashes = countOfUniqueHashes;
            this.status = status;
        }

        @Override
        public String toString()
        {
            return "StatusRecord{" +
                    "countOfFiles=" + countOfFiles +
                    ", countOfHashesCollected=" + countOfHashesCollected +
                    ", countOfUniqueHashes=" + countOfUniqueHashes +
                    ", status=" + status +
                    '}';
        }
    }

    private static class FilenameCollector extends SimpleFileVisitor<Path>
    {
        private final Set<File> filenames;

        public FilenameCollector(Set<File> hashes)
        {
            this.filenames = hashes;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException
        {
            filenames.add(path.toFile());
            return super.visitFile(path, attrs);
        }
    }
}
