package dnt.photoorganiser.imagecompressor;

import com.beust.jcommander.JCommander;
import dnt.photoorganiser.filetime.FileTimeOperations;
import dnt.photoorganiser.imagecompressor.optimisation.JPEGOptimizerOptimisation;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

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
        try
        {
            return SDF.format(toDate(toLocalDateTime(time)));
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    void start()
    {
        System.out.println(config);
        String workingDirectory = System.getProperty("user.dir");

        for (File file : Arrays.stream(new File(workingDirectory).listFiles()).sorted(Comparator.comparing(File::getName)).toList())
        {
            for (String ext : config.extensions)
            {
                if (file.getName().endsWith("." + ext))
                {
                    try
                    {
                        long originalSize = file.length();
                        FileTime creationTime = getCreationTime(file);
                        FileTime modifiedTime = getModifiedTime(file);
                        FileTime lastAccessedTime = getLastAccessedTime(file);

                        if(!config.execute)
                        {
                            System.out.printf("INFO: %s %s %s %s%n", format(creationTime), format(modifiedTime), format(lastAccessedTime), file.getName());
                            continue;
                        }

                        File filenameWithPostfix = appendPostfix(file);

                        Instant start = Instant.now();
                        new JPEGOptimizerOptimisation(file, filenameWithPostfix, config.quality).optimise();
                        Duration duration = Duration.between(start, Instant.now());
                        long newSize = filenameWithPostfix.length();
                        double conversionRatio = (double)newSize / (double) originalSize;

                        FileTimeOperations.setFileTimes(filenameWithPostfix, modifiedTime, lastAccessedTime, creationTime);

                        //System.out.printf("INFO:%s %s %s %s%n", format(creationTime), format(modifiedTime), format(lastAccessedTime), file.getName());
                        System.out.printf("INFO: %s    %.2f    %,d->%,d    %.2f%n",
                                filenameWithPostfix, conversionRatio, originalSize, newSize, (double)duration.toMillis() / 1000.0);
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private File appendPostfix(File file)
    {
        String absolutePath = file.getAbsolutePath();
        int lastPerido = absolutePath.lastIndexOf(".");
        if(lastPerido < 0) return file;

        String start = absolutePath.substring(0, lastPerido);
        String end = absolutePath.substring(lastPerido);
        return new File(start + "-" + config.postfix + end);
    }
}
