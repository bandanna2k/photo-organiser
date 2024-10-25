package dnt.photoorganiser.organiser;

import dnt.photoorganiser.commands.MakeDirectoryCommand;
import dnt.photoorganiser.commands.MoveCommand;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static dnt.photoorganiser.filetime.FileTimeOperations.*;

public class FileInfoWalker
{
    private static final SimpleDateFormat YEAR =  new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat MONTH = new SimpleDateFormat("MMMM");

    private final Path rootDirectory;
    private final Config config;
    private final Set<String> extensions;

    public FileInfoWalker(Config config)
    {
        this.config = config;
        this.rootDirectory = Path.of(System.getProperty("user.dir"));
        this.extensions = new HashSet<>(config.extensions);
    }

    public void walkDirectory() throws IOException
    {
//        System.out.println("INFO: Root dir " + rootDirectory);

        Files.walkFileTree(rootDirectory, new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
            {
                for (File file : dir.toFile().listFiles())
                {
                    if(!file.isFile())
                    {
                        continue;
                    }
                    if(!extensions.isEmpty() &&
                            extensions.stream().noneMatch(x -> file.getName().endsWith("." + x)))
                    {
                        continue;
                    }

//                    System.out.println("INFO: File " + file);

                    Date dateTime = getDateTime(file);

                    Path sourceFilePath = rootDirectory.relativize(file.toPath());
                    Path destinationFilePath = rootDirectory.resolve(YEAR.format(dateTime)).resolve(MONTH.format(dateTime));
                    destinationFilePath = rootDirectory.relativize(destinationFilePath);

                    MakeDirectoryCommand makeDirectoryCommand = new MakeDirectoryCommand(destinationFilePath);
                    MoveCommand moveCommand = new MoveCommand(sourceFilePath, destinationFilePath);

                    if(config.isPreviewMode())
                    {
                        System.out.printf("Preview: %s %s%n", makeDirectoryCommand, moveCommand);
                        continue;
                    }

                    makeDirectoryCommand.execute().consume(
                            returnCodeMkdir ->
                            {
                                moveCommand.execute()
                                        .consume(returnCodeMove ->
                                                {
                                                    System.out.println("Successfully moved file. " + moveCommand);
                                                },
                                                error ->
                                                        System.err.printf("ERROR: Failed to move file. %s. %s%n", error, moveCommand));
                            }
                            , error ->
                                    System.err.printf("ERROR: Failed to make directory file. %s. %s%n", error, makeDirectoryCommand));
                    }
 //                System.out.println("INFO: Dir  " + dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    private Date getDateTime(File file) throws IOException
    {
        return switch (config.fileDateTimeMode) {
            case Creation -> getCreationTimeAsDate(file);
            case Modified -> getModifiedTimeAsDate(file);
            case LastAccessed -> getLastAccessedTimeAsDate(file);
        };
    }
}
