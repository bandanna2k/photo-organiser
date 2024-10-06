package dnt.photoorganiser.organiser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
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
    private static SimpleDateFormat YEAR =  new SimpleDateFormat("YYYY");
    private static SimpleDateFormat MONTH = new SimpleDateFormat("MMMM");

    private final Path rootDirectory;
    private final PrintStream writer;

    public FileInfoWalker(Path rootDirectory)
    {
        this(rootDirectory, System.out);
    }
    public FileInfoWalker(Path rootDirectory, PrintStream writer)
    {
        this.rootDirectory = rootDirectory;
        this.writer = writer;
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
                        writer.println("# INFO: File " + file);

                        Date creationTime = getCreationTimeAsDate(file);

                        Path sourceFilePath = rootDirectory.relativize(file.toPath());
                        Path destinationFilePath = rootDirectory.resolve(YEAR.format(creationTime)).resolve(MONTH.format(creationTime));
                        destinationFilePath = rootDirectory.relativize(destinationFilePath);
                        writer.println("mkdir -p " + destinationFilePath);
                        writer.printf("mv %s %s%n", wrap(sourceFilePath), wrap(destinationFilePath));
                        writer.println();
                    }
                }
                writer.println("# INFO: Dir  " + dir);
                writer.flush();
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    private String wrap(Path destinationFilePath)
    {
        return "\"" + destinationFilePath + "\"";
    }
}
