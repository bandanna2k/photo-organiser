package dnt.photoorganiser.filetime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FileTimeOperations
{
    public static final OffsetDateTime NOW = OffsetDateTime.now();
    public static final ZoneId ZONE_ID = NOW.toZonedDateTime().getZone();
    public static final ZoneOffset ZONE_OFFSET = NOW.getOffset();

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
        Instant instant = ldt.toInstant(ZONE_OFFSET);
        return Date.from(instant);
    }

    public static Date toDate(FileTime ft)
    {
        return toDate(toLocalDateTime(ft));
    }

    public static Calendar toCalendar(FileTime ft)
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZONE_ID));
        calendar.setTime(toDate(ft));
        return calendar;
    }

    public static LocalDateTime toLocalDateTime(FileTime fileTime)
    {
        return fileTime.toInstant().atZone(ZONE_OFFSET).toLocalDateTime();
    }

    public static FileTime toFileTime(Date date)
    {
        return FileTime.from(date.toInstant());
    }
}
