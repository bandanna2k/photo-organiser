package dnt.photoorganiser.filetime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class FileTimeOperations
{
    public static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

    public static void setFileTimes(File file,
                                    FileTime lastModifiedTime,
                                    FileTime lastAccessedTime,
                                    FileTime creationTime) throws IOException
    {
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        attributes.setTimes(lastModifiedTime, lastAccessedTime, creationTime);
    }

    public static FileTime getCreationTime(File file) throws IOException
    {
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        return attributes.readAttributes().creationTime();
    }

    public static FileTime getLastAccessedTime(File file) throws IOException
    {
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        return attributes.readAttributes().lastAccessTime();
    }

    public static FileTime getModifiedTime(File file) throws IOException
    {
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        return attributes.readAttributes().lastModifiedTime();
    }

    public static Date toDate(LocalDateTime ldt)
    {
        Instant instant = ldt.toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }

    public static LocalDateTime toLocalDateTime(FileTime fileTime)
    {
        return fileTime.toInstant().atZone(ZONE_OFFSET).toLocalDateTime();
    }
}
