package dnt.photoorganiser.organiser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class FileTestBase
{
    public static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

    protected static final byte[] CONTENT = "CONTENT".getBytes();
    protected static final byte[] EIGHT_BYTES = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
    protected static final LocalDateTime MORNING = LocalDateTime.of(2023, 10, 17, 8, 55, 00);
    protected static final LocalDateTime NOON = LocalDateTime.of(2023, 10, 17, 12, 56, 00);
    protected static final LocalDateTime NIGHT = LocalDateTime.of(2023, 10, 17, 20, 57, 00);

    public static File writeFile(File file, byte[] content, LocalDateTime created, LocalDateTime modified, LocalDateTime accessed) throws IOException
    {
        try (FileOutputStream fos = new FileOutputStream(file))
        {
            try (BufferedOutputStream bos = new BufferedOutputStream(fos))
            {
                bos.write(content, 0, content.length);
                bos.flush();
            }
        }
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        attributes.setTimes(
                FileTime.fromMillis(modified.toInstant(ZONE_OFFSET).toEpochMilli()),
                FileTime.fromMillis(accessed.toInstant(ZONE_OFFSET).toEpochMilli()),
                FileTime.fromMillis(created.toInstant(ZONE_OFFSET).toEpochMilli())
        );
        return file;
    }

    public static void setFileTimes(File file, LocalDateTime creationTime, LocalDateTime modifiedTime, LocalDateTime accessedTime)
    {
        try
        {
            Files.setAttribute(file.toPath(), "creationTime", toFileTime(creationTime));
            Files.setAttribute(file.toPath(), "lastModifiedTime", toFileTime(modifiedTime));
            Files.setAttribute(file.toPath(), "lastAccessTime", toFileTime(accessedTime));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static FileTime toFileTime(LocalDateTime dateTime)
    {
        Instant instant = dateTime.toInstant(ZONE_OFFSET);
        return toFileTime(instant.toEpochMilli());
    }

    public static FileTime toFileTime(long epochMillis)
    {
        return FileTime.fromMillis(epochMillis);
    }

    public static LocalDateTime getCreationTime(File file)
    {
        try
        {
            BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
            FileTime fileTime = attributes.readAttributes().creationTime();
            return fileTime.toInstant().atZone(ZONE_OFFSET).toLocalDateTime();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static LocalDateTime getLastAccessedTime(File file)
    {
        try
        {
            BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
            FileTime fileTime = attributes.readAttributes().lastAccessTime();
            return fileTime.toInstant().atZone(ZONE_OFFSET).toLocalDateTime();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static LocalDateTime getModifiedTime(File file)
    {
        try
        {
            BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
            FileTime fileTime = attributes.readAttributes().lastModifiedTime();
            return fileTime.toInstant().atZone(ZONE_OFFSET).toLocalDateTime();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
