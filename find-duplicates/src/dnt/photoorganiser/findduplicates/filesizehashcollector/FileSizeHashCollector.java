package dnt.photoorganiser.findduplicates.filesizehashcollector;

import dnt.photoorganiser.findduplicates.hashing.Hasher;
import dnt.photoorganiser.findduplicates.hashing.MD5Hasher;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/*
## Duplicates

Input:
`find_duplicate_images <source> <dest> <archive_directory>`

Output:
```
mv /tmp/Pit/tmp/IMG0001.jpg /tmp/Album/2024/September/

mkdir /tmp/Archive/tmpr
mv /tmp/Pit/tmp/IMG0001_Copy.jpg /tmp/Archive/tmp/
```
 */
public class FileSizeHashCollector
{
//    private static final String SIXTY_SPACES = "                                                            ";

    private final Path sourceDirectory;

    private final Map<SizeHash, List<Path>> sizeHashToFiles = new HashMap<>();
    private final Hasher hasher;
    private final Set<String> extensionsLowerCase = new HashSet<>();
    private final boolean allFiles;

    public FileSizeHashCollector(Path sourceDirectory, boolean allFiles, Set<String> extensions)
    {
        extensions.forEach(extension -> extensionsLowerCase.add(extension.toLowerCase()));
        this.allFiles = allFiles;
        this.hasher = new MD5Hasher();
        this.sourceDirectory = sourceDirectory;
    }

    public void walkSource() throws IOException
    {
        AtomicInteger fileCounter = new AtomicInteger(1);
        System.out.println("INFO: Walking directory: " + sourceDirectory);
        Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException
            {
                if(
                        allFiles ||
                        extensionsLowerCase.stream().anyMatch(extension -> filePath.toString().toLowerCase().endsWith("." + extension)))
                {
                    File file = filePath.toFile();

                    long start = System.currentTimeMillis();
                    SizeHash sizeHash = new SizeHash(file.length(), hasher.hash(filePath));
                    double duration = (System.currentTimeMillis() - start) / 1000.0;

                    List<Path> files = sizeHashToFiles.computeIfAbsent(sizeHash, sh -> new ArrayList<>());
                    files.add(filePath);

                    System.out.printf("Count: %d, Hash time: %.1f, File: %s\n",
                            fileCounter.getAndIncrement(), duration, filePath);
                }
                return super.visitFile(filePath, attrs);
            }
        });
        System.out.println();
        System.out.println("INFO: Finished: " + sourceDirectory);
    }

    public void forEachSizeHash(BiConsumer<SizeHash, List<Path>> consumer)
    {
        int size = sizeHashToFiles.size();
        AtomicInteger counter = new AtomicInteger(1);
        sizeHashToFiles.entrySet().stream()
                .sorted((entry1, entry2) ->
                {
                    if(entry1.getValue().size() < entry2.getValue().size()) return 1;
                    if(entry1.getValue().size() > entry2.getValue().size()) return -1;
                    return 0;
                }).forEach(entry -> {
                    consumer.accept(entry.getKey(), entry.getValue());
                    System.out.printf("INFO: Progress, %d of %d complete.", counter.getAndIncrement(), size);
                });
    }
}
