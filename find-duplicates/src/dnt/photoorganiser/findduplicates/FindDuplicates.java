package dnt.photoorganiser.findduplicates;

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
public class FindDuplicates
{
    private final Path sourceDirecotry;

    private Map<Long, SizeRecord> sizeToFileData = new HashMap<>();

    public FindDuplicates(Path sourceDirectory)
    {
        this.sourceDirecotry = sourceDirectory;
    }

    public void walkSource() throws IOException
    {
        Files.walkFileTree(sourceDirecotry, new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException
            {
                File file = filePath.toFile();
                SizeRecord sizeRecord = sizeToFileData.computeIfAbsent(file.length(), SizeRecord::new);

                FileData fileData = new FileData(filePath);
                sizeRecord.listOfFileData.add(fileData);

                sizeRecord.processAnyHashes();

                return super.visitFile(filePath, attrs);
            }
        });
    }

    public void forEachDuplicate(BiConsumer<Long, List<FileData>> consumer)
    {
        sizeToFileData.forEach((size, sizeRecord) -> {
            if(sizeRecord.listOfFileData.size() > 1)
            {
                consumer.accept(size, sizeRecord.listOfFileData);
            }
        });
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
            if(listOfFileData.size() > 1)
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
            if(hash.isEmpty())
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
