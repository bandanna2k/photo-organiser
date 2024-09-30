package dnt.photoorganiser.findduplicates;

import dnt.photoorganiser.findduplicates.archiver.Archiver;
import dnt.photoorganiser.findduplicates.filesizehashcollector.FileSizeHashCollector;
import dnt.photoorganiser.findduplicates.filesizehashcollector.SizeHash;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDuplicates
{
    private final Chooser chooser;
    private final FileSizeHashCollector primaryDirectoryCollector;
    private final FileSizeHashCollector pitDirectoryCollector;
    private final Path archiveDirectory;
    private final Archiver archiver;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy_HH-mm-ss");
    private int counter = 1;

    public FindDuplicates(Path primaryDirectory,
                          Path pitDirectory,
                          Path archiveDirectory,
                          Chooser chooser,
                          Archiver archiver)
    {
        primaryDirectoryCollector = new FileSizeHashCollector(primaryDirectory);
        pitDirectoryCollector = new FileSizeHashCollector(pitDirectory);

        this.archiveDirectory = archiveDirectory;
        this.chooser = chooser;
        this.archiver = archiver;
    }

//    public void forEachBatchOfCommands2(Consumer<BatchOfCommands> consumer) throws IOException
//    {
//        primaryDirectoryCollector.walkSource();
//        pitDirectoryCollector.walkSource();
//
//        Map<SizeHash, List<Path>> primarySizeHashToFiles = new HashMap<>();
//        primaryDirectoryCollector.forEachSizeHash((sizeHash, paths) -> {
//            assert paths.size() == 1;
//            primarySizeHashToFiles.put(sizeHash, paths);
//        });
//
//        Date dateTime = new Date();
//        pitDirectoryCollector.forEachSizeHash((sizeHash, files) -> {
//            List<Path> primaryFiles = primarySizeHashToFiles.get(sizeHash);
//            if(null == primaryFiles)
//            {
//                // Not found in primary directory
//                if(files.size() > 1)
//                {
//                    // Duplicates found - Choose, archive
//                    int choice = chooser.choose(files);
//
//                    String filename = String.format("duplicates.%s.%04d.tar.gz", sdf.format(dateTime), counter++);
//                    Path archiveFilePath = archiveDirectory.resolve(filename);
//                    ArchiveCommands.Tar archiveCommand = new ArchiveCommands.Tar(pitDirectoryCollector.sourceDirectory, archiveFilePath);
//                    for (int i = 0; i < files.size(); i++)
//                    {
//                        Path file = files.get(i);
//                        if(i == choice)
//                        {
//                            // Leave
//                        }
//                        else
//                        {
//                            // Archive
//                            archiveCommand.withSource(file);
//                        }
//                    }
//                    consumer.accept(new BatchOfCommands().with(archiveCommand));
//                }
//                else if(files.size() == 1)
//                {
//                    // Unique file, just move to Album
//                }
//                else
//                {
//                    throw new RuntimeException("Bad file size. " + files.size());
//                }
//            }
//            else
//            {
//                // Found in primary directory
//                // Archive all files
//                String filename = String.format("duplicates.%s.%4d.tar.gz", sdf.format(dateTime), counter++);
//                Path archiveFilePath = archiveDirectory.resolve(filename);
//                ArchiveCommands.Tar command = new ArchiveCommands.Tar(pitDirectoryCollector.sourceDirectory, archiveFilePath);
//                files.forEach(command::withSource);
//                consumer.accept(new BatchOfCommands().with(command));
//            }
//        });
//    }

    public void forEachDuplicates() throws IOException
    {
        primaryDirectoryCollector.walkSource();
        pitDirectoryCollector.walkSource();

        Map<SizeHash, List<Path>> primarySizeHashToFiles = new HashMap<>();
        primaryDirectoryCollector.forEachSizeHash((sizeHash, paths) -> {
            assert paths.size() == 1;
            primarySizeHashToFiles.put(sizeHash, paths);
        });

        Date dateTime = new Date();
        pitDirectoryCollector.forEachSizeHash((sizeHash, files) -> {
            List<Path> primaryFiles = primarySizeHashToFiles.get(sizeHash);
            if(null == primaryFiles)
            {
                // Not found in primary directory
                if(files.size() > 1)
                {
                    // Duplicates found - Choose, archive
                    int choice = chooser.choose(files);

                    for (int i = 0; i < files.size(); i++)
                    {
                        Path file = files.get(i);
                        if(i == choice)
                        {
                            // Leave
                        }
                        else
                        {
                            // Archive
                            archiver.add(file);
                        }
                    }
                }
                else if(files.size() == 1)
                {
                    // Unique file, just move to Album
                }
                else
                {
                    throw new RuntimeException("Bad file size. " + files.size());
                }
            }
            else
            {
                // Found in primary directory
                // Archive all files
                files.forEach(archiver::add);
            }
        });
    }
}
