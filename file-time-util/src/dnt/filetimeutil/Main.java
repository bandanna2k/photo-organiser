package dnt.filetimeutil;

import com.beust.jcommander.JCommander;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static dnt.photoorganiser.filetime.FileTimeOperations.*;

public class Main
{
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat SDFX = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");

    private final Config config;

    public static void main(String[] args) throws IOException
    {
        new Main(args).start();
    }

    static Config getConfig(String[] args)
    {
        Config config = new Config();
        JCommander.newBuilder().addObject(config).args(args).build();
        return config;
    }

    public Main(String[] args)
    {
        this(getConfig(args));
    }

    public Main(Config config)
    {
        this.config = config;
    }

    int start() throws IOException
    {
        AtomicInteger count = new AtomicInteger(0);
        Files.walkFileTree(config.path(), new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
            {
                final Optional<Integer> maybeYear = maybeGetYear(file.getParent());
                final Optional<Integer> maybeMonth = maybeGetMonth(file.getParent());
                maybeYear.ifPresent(pathYear -> {
                    maybeMonth.ifPresent(pathMonth -> {
                        try
                        {
                            FileTime creationTime = getCreationTime(file.toFile());
                            FileTime modifiedTime = getModifiedTime(file.toFile());
                            FileTime lastAccessedTime = getLastAccessedTime(file.toFile());

//                            System.out.printf("INFO: %s %s %s%n", creationTime, modifiedTime, lastAccessedTime);

                            Calendar creationDate = toCalendar(creationTime);
                            Calendar modifiedDate = toCalendar(modifiedTime);
                            Calendar lastAccessedDate = toCalendar(lastAccessedTime);

//                            System.out.printf("INFO: CD %s, MD %s, LAT %s%n",
//                                    SDFX.format(creationDate.getTime()),
//                                    SDFX.format(modifiedDate.getTime()),
//                                    SDFX.format(lastAccessedDate.getTime()));

                            final int fileMonth;
                            final int fileYear;
                            switch(config.fileTimeType)
                            {
                                case CreationTime ->
                                {
                                    fileMonth = creationDate.get(Calendar.MONTH);
                                    fileYear = creationDate.get(Calendar.YEAR);
                                }
                                case ModifiedTime ->
                                {
                                    fileMonth = modifiedDate.get(Calendar.MONTH);
                                    fileYear = modifiedDate.get(Calendar.YEAR);
                                }
                                case LastAccessedTime ->
                                {
                                    fileMonth = lastAccessedDate.get(Calendar.MONTH);
                                    fileYear = lastAccessedDate.get(Calendar.YEAR);
                                }
                                default -> throw new IllegalArgumentException("Invalid file time type. " + config.fileTimeType);
                            }

                            Calendar newCreationTime = toCalendar(creationTime);
                            newCreationTime.set(Calendar.YEAR, pathYear);
                            newCreationTime.set(Calendar.MONTH, pathMonth);

                            Calendar newModifiedTime = toCalendar(modifiedTime);
                            newModifiedTime.set(Calendar.YEAR, pathYear);
                            newModifiedTime.set(Calendar.MONTH, pathMonth);

                            if(fileMonth != pathMonth || fileYear != pathYear)
                            {
                                if(config.execute)
                                {
                                    setFileTimes(file.toFile(), toFileTime(newModifiedTime.getTime()), lastAccessedTime, toFileTime(newCreationTime.getTime()));
                                    newCreationTime = toCalendar(getCreationTime(file.toFile()));
                                    newModifiedTime = toCalendar(getModifiedTime(file.toFile()));

                                    System.out.printf("INFO: [%s] Changed modified time from '%s' to '%s', modified time from '%s' to '%s'.%n",
                                            file.toFile().getAbsolutePath(),
                                            SDFX.format(modifiedDate.getTime()),
                                            SDFX.format(newModifiedTime.getTime()),
                                            SDFX.format(creationDate.getTime()),
                                            SDFX.format(newCreationTime.getTime())
                                    );
                                    count.getAndIncrement();
                                }
                                else
                                {
                                    System.out.printf("INFO: [%s] Suggest changing modified time from '%s' to '%s', creation time from '%s' to '%s'.%n",
                                            file.toFile().getAbsolutePath(),
                                            SDFX.format(modifiedDate.getTime()),
                                            SDFX.format(newModifiedTime.getTime()),
                                            SDFX.format(creationDate.getTime()),
                                            SDFX.format(newCreationTime.getTime())
                                    );
                                }
                            }
                        }
                        catch (IOException e)
                        {
                            System.out.println("ERROR: " + e.getMessage());
                            throw new RuntimeException(e);
                        }
                    });
                });
                return super.visitFile(file, attrs);
            }
        });
        return count.get();
    }

    private Optional<Integer> maybeGetMonth(Path dir)
    {
        for (Path path : dir)
        {
            String dirName = path.toFile().getName();
            if (0 == "january".compareToIgnoreCase(dirName)) return Optional.of(0);
            if (0 == "february".compareToIgnoreCase(dirName)) return Optional.of(1);
            if (0 == "march".compareToIgnoreCase(dirName)) return Optional.of(2);
            if (0 == "april".compareToIgnoreCase(dirName)) return Optional.of(3);
            if (0 == "may".compareToIgnoreCase(dirName)) return Optional.of(4);

            if (0 == "june".compareToIgnoreCase(dirName)) return Optional.of(5);
            if (0 == "july".compareToIgnoreCase(dirName)) return Optional.of(6);
            if (0 == "august".compareToIgnoreCase(dirName)) return Optional.of(7);
            if (0 == "september".compareToIgnoreCase(dirName)) return Optional.of(8);
            if (0 == "october".compareToIgnoreCase(dirName)) return Optional.of(9);

            if (0 == "november".compareToIgnoreCase(dirName)) return Optional.of(10);
            if (0 == "december".compareToIgnoreCase(dirName)) return Optional.of(11);
        }
        return Optional.empty();
    }

    private Optional<Integer> maybeGetYear(Path dir)
    {
        for (Path path : dir)
        {
            String dirName = path.toFile().getName();
            if(dirName.length() == 4)
            {
                boolean isNumeric = dirName.chars().allMatch(Character::isDigit);
                if (isNumeric)
                {
                    return Optional.of(Integer.parseInt(dirName));
                }
            }
        }
        return Optional.empty();
    }
}
