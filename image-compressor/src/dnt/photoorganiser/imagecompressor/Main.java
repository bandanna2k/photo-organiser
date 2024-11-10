package dnt.photoorganiser.imagecompressor;

import com.beust.jcommander.JCommander;
import dnt.common.Result;
import dnt.photoorganiser.commands.DeleteCommand;
import dnt.photoorganiser.filetime.FileTimeOperations;
import dnt.photoorganiser.imagecompressor.optimisation.JPEGOptimizerOptimisation;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static dnt.photoorganiser.filetime.FileTimeOperations.*;

public class Main
{
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

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");

    private static String format(FileTime time)
    {
        return SDF.format(toDate(toLocalDateTime(time)));
    }

    int start()
    {
        try
        {
            System.out.println(config);
            Path workingDirectory = Path.of(System.getProperty("user.dir"));

            AtomicInteger fileToExecute = new AtomicInteger(0);
            List<Path> files = new ArrayList<>();

            Files.walk(workingDirectory).forEach(path ->
            {
                if (shouldContinue(path.toFile()))
                {
                    fileToExecute.getAndIncrement();
                    files.add(path);
                }
            });

            for (Path path : files)
            {
                File file = path.toFile();
                File filenameWithPostfix = appendPostfix(file);
                if (filenameWithPostfix.exists() )
                {
                    System.out.printf("WARN: Skipping, file already exists. %s%n", file.getAbsolutePath());
                    continue;
                }
                if(filenameWithPostfix.getName().contains("-" + config.postfix + "-" + config.postfix))
                {
                    System.out.printf("WARN: Skipping, file already compressed. %s%n", file.getAbsolutePath());
                    continue;
                }

                try
                {
                    long originalSize = file.length();
                    FileTime creationTime = getCreationTime(file);
                    FileTime modifiedTime = getModifiedTime(file);
                    FileTime lastAccessedTime = getLastAccessedTime(file);

                    if (config.execute)
                    {
                        Instant start = Instant.now();
                        new JPEGOptimizerOptimisation(file, filenameWithPostfix, config.quality).optimise();
                        Duration duration = Duration.between(start, Instant.now());
                        long newSize = filenameWithPostfix.length();
                        double conversionRatio = (double) newSize / (double) originalSize;

                        setFileTimes(filenameWithPostfix, modifiedTime, lastAccessedTime, creationTime);
                        System.out.printf("INFO: %.2f    %s    %,d->%,d    %.2f%n",
                                conversionRatio, filenameWithPostfix, originalSize, newSize, (double) duration.toMillis() / 1000.0);
                    }
                    else
                    {
                        System.out.printf("INFO: %s %s %s %s%n", format(creationTime), format(modifiedTime), format(lastAccessedTime), file.getAbsolutePath());
                    }
                }
                catch (IOException e)
                {
                    Result<Integer, String> result = new DeleteCommand(filenameWithPostfix.toPath()).execute();
                    result.consumeIfFailure(v -> System.out.printf("ERROR: Failed to delete file. %s %s%n", filenameWithPostfix.getName(), v));
                    throw new RuntimeException(e);
                }
                catch (Exception e)
                {
                    System.out.printf("ERROR: %s %s%n", file.getName(), e.getMessage());
                    Result<Integer, String> result = new DeleteCommand(filenameWithPostfix.toPath()).execute();
                    result.consumeIfFailure(v -> System.out.printf("ERROR: Failed to delete file. %s %s%n", filenameWithPostfix.getName(), v));
                }
            }
            return fileToExecute.get();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private boolean shouldContinue(File file)
    {
        boolean doContinue = true;

        if (!config.extensions.isEmpty())
        {
            boolean extensionMatches = false;
            for (String ext : config.extensions)
            {
                if (file.getName().endsWith("." + ext))
                {
                    extensionMatches = true;
                }
            }
            doContinue &= extensionMatches;
        }
        if (config.contains().isPresent() && !file.getName().contains(config.contains().get()))
        {
            doContinue = false;
        }
        if (config.regex().isPresent())
        {
            String regex = config.regex().get();
            if (!file.getName().matches(regex))
            {
                doContinue = false;
            }
        }
        return doContinue;
    }

    private File appendPostfix(File file)
    {
        String absolutePath = file.getAbsolutePath();
        int lastPerido = absolutePath.lastIndexOf(".");
        if (lastPerido < 0)
        {
            return file;
        }

        String start = absolutePath.substring(0, lastPerido);
        String end = absolutePath.substring(lastPerido);
        return new File(start + "-" + config.postfix + end);
    }
}
