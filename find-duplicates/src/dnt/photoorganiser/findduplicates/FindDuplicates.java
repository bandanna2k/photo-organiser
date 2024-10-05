package dnt.photoorganiser.findduplicates;

import dnt.photoorganiser.findduplicates.archiver.Archiver;
import dnt.photoorganiser.findduplicates.choosers.Chooser;
import dnt.photoorganiser.findduplicates.filesizehashcollector.FileSizeHashCollector;
import dnt.photoorganiser.findduplicates.filesizehashcollector.SizeHash;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class FindDuplicates
{
    private final Chooser chooser;
    private final FileSizeHashCollector primaryDirectoryCollector;
    private final FileSizeHashCollector pitDirectoryCollector;
    private final Archiver archiver;

    public FindDuplicates(Path primaryDirectory,
                          Path pitDirectory,
                          Chooser chooser,
                          Archiver archiver,
                          boolean allFiles,
                          Set<String> extensions)
    {
        primaryDirectoryCollector = new FileSizeHashCollector(primaryDirectory, allFiles, extensions);
        pitDirectoryCollector = new FileSizeHashCollector(pitDirectory, allFiles, extensions);

        this.chooser = chooser;
        this.archiver = archiver;
    }

    public void findAndArchive() throws IOException
    {
        primaryDirectoryCollector.walkSource();
        int pitFileCount = pitDirectoryCollector.walkSource();

        Map<SizeHash, List<Path>> primarySizeHashToFiles = new HashMap<>();
        primaryDirectoryCollector.forEachSizeHash((sizeHash, paths) -> {
//            assert paths.size() == 1;
            if (paths.size() > 1) System.err.println("WARNING: Duplicates in primary directory. " + paths);
            primarySizeHashToFiles.put(sizeHash, paths);
        });

        // Loop through non-interactive archiving first
        pitDirectoryCollector.forEachSizeHash((sizeHash, files) -> {
            List<Path> primaryFiles = primarySizeHashToFiles.get(sizeHash);
            if(null != primaryFiles)
            {
                // Found in primary directory
                // Archive all files
                files.forEach(archiver::add);
            }
        });
        // Loop through maybe-interactive archiving
        AtomicInteger counter = new AtomicInteger(1);
        pitDirectoryCollector.forEachSizeHash((sizeHash, files) -> {
            System.out.printf("INFO: Progress, %d of %d complete.%n", counter.getAndIncrement(), pitFileCount);
            chooseAndArchive(sizeHash, files, primarySizeHashToFiles);
        });
    }

    private void chooseAndArchive(SizeHash sizeHash, List<Path> files, Map<SizeHash, List<Path>> primarySizeHashToFiles)
    {
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
                // Unique file, just move to leave.
            }
            else
            {
                throw new RuntimeException("Bad file size. " + files.size());
            }
        }
        else
        {
            // Covered in a separate loop.
//            // Found in primary directory
//            // Archive all files
//            files.forEach(archiver::add);
        }
    }
}
