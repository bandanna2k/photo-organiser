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
    public final Path sourceDirectory;

    private final Map<SizeHash, List<Path>> sizeHashToFiles = new HashMap<>();
    private final Hasher hasher;

    public FileSizeHashCollector(Path sourceDirectory)
    {
        this.hasher = new MD5Hasher();
        this.sourceDirectory = sourceDirectory;
    }

    public void walkSource() throws IOException
    {
        Files.walkFileTree(sourceDirectory, new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException
            {
                File file = filePath.toFile();
                SizeHash sizeHash = new SizeHash(file.length(), hasher.hash(filePath));
                List<Path> files = sizeHashToFiles.computeIfAbsent(sizeHash, sh -> new ArrayList<>());
                files.add(filePath);

                return super.visitFile(filePath, attrs);
            }
        });
    }

    public void forEachSizeHash(BiConsumer<SizeHash, List<Path>> consumer)
    {
        sizeHashToFiles.forEach(consumer);
    }
}
