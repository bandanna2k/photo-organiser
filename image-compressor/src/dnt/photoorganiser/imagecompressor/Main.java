package dnt.photoorganiser.imagecompressor;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import dnt.photoorganiser.filetime.FileTimeOperations;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import static dnt.photoorganiser.filetime.FileTimeOperations.toDate;

public class Main
{
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
        System.out.println(config);
        String workingDirectory = System.getProperty("user.dir");
        System.out.println("INFO:" + workingDirectory);

        File dir = new File(workingDirectory);
        for (File file : dir.listFiles())
        {
            for (String ext : config.extensions)
            {
                if(file.getName().endsWith("." + ext))
                {
                    try
                    {
                        LocalDateTime creationTime = FileTimeOperations.getCreationTime(file);
                        LocalDateTime modifiedTime = FileTimeOperations.getModifiedTime(file);
                        LocalDateTime lastAccessedTime = FileTimeOperations.getLastAccessedTime(file);
                        System.out.printf("INFO:%s %s %s %s%n", file.getName(), format(creationTime), modifiedTime, lastAccessedTime);
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");
    private static String format(LocalDateTime time)
    {
        try
        {
            return SDF.format(toDate(time.toLocalDate().atStartOfDay()));
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    void start()
    {
    }
}
