package dnt.photoorganiser.organiser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static dnt.photoorganiser.organiser.FileTimeOperations.getCreationTime;
import static dnt.photoorganiser.organiser.FileTimeOperations.getCreationTimeAsDate;

public class FileInfoWalker
{
    private final Path rootDirectory;

    private static SimpleDateFormat YEAR =  new SimpleDateFormat("YYYY");
    private static SimpleDateFormat MONTH = new SimpleDateFormat("MMMM");

    public FileInfoWalker(Path rootDirectory)
    {
        this.rootDirectory = rootDirectory;
    }

    public void walkDirectory() throws IOException
    {
        Files.walkFileTree(rootDirectory, new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
            {
                for (File file : dir.toFile().listFiles())
                {
                    if (file.isFile())
                    {
//                        System.out.println("INFO: File " + file);

                        Date creationTime = getCreationTimeAsDate(file);

                        Path sourceFilePath = rootDirectory.relativize(file.toPath());
                        Path destinationFilePath = rootDirectory.resolve(YEAR.format(creationTime)).resolve(MONTH.format(creationTime));
                        destinationFilePath = rootDirectory.relativize(destinationFilePath);
                        System.out.println("mkdir -p " + destinationFilePath);
                        System.out.printf("mv %s %s%n", sourceFilePath, destinationFilePath);
                        System.out.println();

                    }
                }
//                System.out.println("INFO: Dir  " + dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }
}
