package dnt.photoorganiser.findduplicates;

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
public class CollectSizeHashFiles
{
    private final Path sourceDirectory;

    private Map<SizeHash, List<Path>> sizeHashToFiles = new HashMap<>();
    private Hasher hasher;

    public CollectSizeHashFiles(Path sourceDirectory)
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

    private static class SizeRecord
    {
        final long size;
        final List<FileData> listOfFileData = new ArrayList<>();

        public SizeRecord(Long size)
        {
            this.size = size;
        }

        public void processAnyHashes()
        {
            if (listOfFileData.size() > 1)
            {
                listOfFileData.forEach(FileData::setHash);
            }
        }
    }

    public static class FileData
    {
        final Path filePath;
        Optional<String> hash = Optional.empty();

        private FileData(Path filePath)
        {
            this.filePath = filePath;
        }

        public void setHash()
        {
            if (hash.isEmpty())
            {
                hash = Optional.of("sdfsdfs");
            }
        }

        @Override
        public String toString()
        {
            return "FileData{" +
                    "filePath=" + filePath +
                    ", hash=" + hash +
                    '}';
        }
    }
}
