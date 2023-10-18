package photo.organiser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

public abstract class Utilities
{
    public static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

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

    private static FileTime toFileTime(LocalDateTime timeInThePast)
    {
        Instant instant = timeInThePast.toInstant(ZONE_OFFSET);
        return FileTime.fromMillis(instant.toEpochMilli());
    }
}
