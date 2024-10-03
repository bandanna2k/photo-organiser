package dnt.photoorganiser.organiser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class FileTimeOperations
{
    public static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();

    public static LocalDateTime getCreationTime(File file) throws IOException
    {
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        FileTime fileTime = attributes.readAttributes().creationTime();
        return fileTime.toInstant().atZone(ZONE_OFFSET).toLocalDateTime();
    }

    public static Date getCreationTimeAsDate(File file) throws IOException
    {
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        FileTime fileTime = attributes.readAttributes().creationTime();
        return new Date(fileTime.toInstant().toEpochMilli());
    }

    public static LocalDateTime getLastAccessedTime(File file) throws IOException
    {
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        FileTime fileTime = attributes.readAttributes().lastAccessTime();
        return fileTime.toInstant().atZone(ZONE_OFFSET).toLocalDateTime();
    }

    public static LocalDateTime getModifiedTime(File file) throws IOException
    {
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        FileTime fileTime = attributes.readAttributes().lastModifiedTime();
        return fileTime.toInstant().atZone(ZONE_OFFSET).toLocalDateTime();
    }

}
