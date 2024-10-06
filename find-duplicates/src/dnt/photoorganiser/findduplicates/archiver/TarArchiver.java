package dnt.photoorganiser.findduplicates.archiver;

import dnt.common.Result;
import dnt.photoorganiser.commands.ArchiveCommands;

import java.io.Closeable;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TarArchiver implements Archiver, Closeable
{
    private final String dateTimeString;
    private int counter = 1;

    private final List<Path> filePaths = new ArrayList<>();

    private final String prefix;
    private final Path workingDirectory;
    private final Path archiveDirectory;
    private final int maxFiles;

    public TarArchiver(String prefix, Path workingDirectory, Path archiveDirectory)
    {
        this(prefix, workingDirectory, archiveDirectory, 10);
    }
    public TarArchiver(String prefix, Path workingDirectory, Path archiveDirectory, int maxFiles)
    {
        this.maxFiles = maxFiles;
        this.prefix = prefix;
        this.workingDirectory = workingDirectory;
        this.archiveDirectory = archiveDirectory;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        this.dateTimeString = sdf.format(new Date());
    }

    @Override
    public void close()
    {
        archive();
    }

    void maybeArchive()
    {
        if(filePaths.size() < maxFiles) return;

        archive();
    }
    void archive()
    {
        if(filePaths.isEmpty()) return;

        System.out.printf("INFO: Archiving %d files. %s%n", filePaths.size(), filePaths);

        String filename = String.format("%s.%s.%04d.tar.gz", prefix, dateTimeString, counter++);
        ArchiveCommands.Tar tarCommand = new ArchiveCommands.Tar(workingDirectory, archiveDirectory.resolve(filename));
        filePaths.forEach(tarCommand::withSource);
        Result<Integer, String> result = tarCommand.execute();
        result.consume(
                success -> {
                    if(success != 0 )
                    {
                        System.err.println("WARNING: Tar command failed. Error code. " + success);
                        System.err.println("WARNING: " + Arrays.toString(tarCommand.getCommand()));
                    }
                    filePaths.clear();
                },
                failure -> {
                    System.err.println("ERROR: " + failure);
                });
    }

    @Override
    public void add(Path filePath)
    {
        filePaths.add(filePath);
        maybeArchive();
    }
}
