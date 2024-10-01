package dnt.photoorganiser.findduplicates.archiver;

import dnt.common.Result;
import dnt.photoorganiser.commands.ArchiveCommands;

import java.io.Closeable;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TarArchiver implements Archiver, Closeable
{
    private final String dateTimeString;
    private int counter = 1;

    private List<Path> filePaths = new ArrayList<>();

    private final String prefix;
    private final Path workingDirectory;
    private final Path archiveDirectory;

    public TarArchiver(String prefix, Path workingDirectory, Path archiveDirectory)
    {
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
        if(filePaths.size() < 10) return;

        archive();
    }
    void archive()
    {
        if(filePaths.isEmpty()) return;

        String filename = String.format("%s.%s.%04d.tar.gz", prefix, dateTimeString, counter++);
        ArchiveCommands.Tar tarCommand = new ArchiveCommands.Tar(workingDirectory, archiveDirectory.resolve(filename));
        filePaths.forEach(tarCommand::withSource);
        Result<Integer, String> result = tarCommand.execute();
        result.fold(
                success -> {
                    assert success == 0 : "Error code of " + success;
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
