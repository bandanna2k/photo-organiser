package dnt.photoorganiser.findduplicates.filesizehashcollector;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public int walkSource() throws IOException
    {
        AtomicInteger fileCounter = new AtomicInteger(0);
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
                            fileCounter.incrementAndGet(), duration, filePath);
                }
                return super.visitFile(filePath, attrs);
            }
        });
        System.out.println();
        System.out.println("INFO: Finished: " + sourceDirectory);
        return fileCounter.get();
    }

    public void forEachSizeHash(BiConsumer<SizeHash, List<Path>> consumer)
    {
        sizeHashToFiles.entrySet().stream()
                .sorted((entry1, entry2) ->
                {
                    if(entry1.getValue().size() < entry2.getValue().size()) return 1;
                    if(entry1.getValue().size() > entry2.getValue().size()) return -1;
                    return 0;
                }).forEach(entry -> {
                    consumer.accept(entry.getKey(), entry.getValue());
                });
    }

    private record LoadSaveRecord(Path path, long size, String hash) {}

    public boolean load(File file)
    {
        if(!file.exists()) return false;
        try
        {
            try
            {
                final ObjectMapper objectMapper = new ObjectMapper();
                LoadSaveRecord[] records = objectMapper.readValue(file, LoadSaveRecord[].class);
                this.sizeHashToFiles.clear();
                for (LoadSaveRecord record : records)
                {
                    List<Path> pathList = sizeHashToFiles.computeIfAbsent(new SizeHash(record.size, record.hash), sizeHash -> new ArrayList<>());
                    pathList.add(record.path);
                }
                System.out.println("INFO: Hash cache loaded. Paths: " + records.length);
                return true;
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        catch (Exception e)
        {
            System.err.println("ERROR: Failed to load. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void save(File file)
    {
        try
        {
            final ObjectMapper objectMapper = new ObjectMapper();
            List<LoadSaveRecord> objects = new ArrayList<>();
            sizeHashToFiles.forEach((sizeHash, paths) -> {
                paths.forEach(path -> {
                    objects.add(new LoadSaveRecord(path, sizeHash.size(), sizeHash.hash()));
                });
            });
            objectMapper.writeValue(file, objects);
            System.out.println("INFO: Hash cache saved. Paths: " + objects.size());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
