package dnt.photoorganiser.organiser;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static java.nio.file.StandardOpenOption.APPEND;
import static org.assertj.core.api.Assertions.*;

public class WriteFileTest extends FileTestBase
{
    private static final byte[] EIGHT_BYTES = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08 };
    private static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();

    @Test
    public void shouldWriteFile() throws IOException
    {
        LocalDateTime created = LocalDateTime.of(2023, 10, 17, 20, 55, 00);
        LocalDateTime modified = LocalDateTime.of(2023, 10, 17, 20, 56, 00);
        LocalDateTime accessed = LocalDateTime.of(2023, 10, 17, 20, 57, 00);
        File file = writeFile(tempDir.newFile(), EIGHT_BYTES, created, modified, accessed);

        FileAssert.assertFile(file)
                .withCreated(created)
                .withModified(modified)
                .withAccessed(accessed);
    }

    @Test
    public void shouldNotChangeCreatedTime() throws IOException
    {
        LocalDateTime timeInThePast = LocalDateTime.of(2000, 1, 31, 23, 58, 59);

        File tempFile = tempDir.newFile();
        setFileTimes(tempFile, timeInThePast, timeInThePast, timeInThePast);

        FileAssert.assertFile(tempFile)
                .withCreated(timeInThePast)
                .withModified(timeInThePast)
                .withAccessed(timeInThePast);

        Files.writeString(tempFile.toPath(), "postfix", APPEND);

        FileAssert.assertFile(tempFile)
                .withCreated(timeInThePast);
        //.withModified(timeInThePast)
        //.withAccessed(timeInThePast);
    }

    private static class FileAssert
    {
        private final BasicFileAttributes basicFileAttributes;

        FileAssert(File file) throws IOException
        {
            basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
//            System.out.println(basicFileAttributes.creationTime());
//            System.out.println(basicFileAttributes.lastAccessTime());
//            System.out.println(basicFileAttributes.lastModifiedTime());
        }

        public static FileAssert assertFile(File file) throws IOException
        {
            return new FileAssert(file);
        }

        public FileAssert withCreated(LocalDateTime expected)
        {
            FileSystem fileSystem = FileSystems.getFileSystem(new File("/").toURI());
            for (FileStore fileStore : fileSystem.getFileStores())
            {
                if("NTFS".equalsIgnoreCase(fileStore.name()))
                {
                    LocalDateTime actual = LocalDateTime.ofInstant(basicFileAttributes.creationTime().toInstant(), ZONE_OFFSET);
                    assertThat(actual).describedAs("creation time").isEqualTo(expected);
                    return this;
                }
                if(fileStore.name().toUpperCase().contains("FAT"))
                {
                    LocalDateTime actual = LocalDateTime.ofInstant(basicFileAttributes.creationTime().toInstant(), ZONE_OFFSET);
                    assertThat(actual).describedAs("creation time").isEqualTo(expected);
                    return this;
                }
            }
            System.out.println("WARNING: Can't check creation date as file system does not support it.");
            return this;
        }

        public FileAssert withModified(LocalDateTime expected)
        {
            LocalDateTime actual = LocalDateTime.ofInstant(basicFileAttributes.lastModifiedTime().toInstant(), ZONE_OFFSET);
            assertThat(actual).describedAs("modified time").isEqualTo(expected);
            return this;
        }

        public FileAssert withAccessed(LocalDateTime expected)
        {
            LocalDateTime actual = LocalDateTime.ofInstant(basicFileAttributes.lastAccessTime().toInstant(), ZONE_OFFSET);
            assertThat(actual).describedAs("accessed time").isEqualTo(expected);
            return this;
        }
    }
}
