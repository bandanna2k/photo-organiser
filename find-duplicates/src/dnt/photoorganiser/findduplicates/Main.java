package dnt.photoorganiser.findduplicates;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import dnt.photoorganiser.findduplicates.archiver.TarArchiver;
import dnt.photoorganiser.findduplicates.choosers.AlphabeticalPathChooser;
import dnt.photoorganiser.findduplicates.choosers.ChooserFactory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class Main implements Closeable
{
    private static final int IO_EXCEPTION = 1000;

    private final FindDuplicates findDuplicates;
    private TarArchiver archiver;

    public static void main(String[] args) throws IOException
    {
        new Main(args).start();
    }
    static Config getConfig(String[] args)
    {
        Config config = new Config();
        JCommander.newBuilder().addObject(config).args(args).build();
        if(config.directories.size() != 3)
        {
            throw new ParameterException("3 directories required.");
        }
        return config;
    }

    public Main(String[] args)
    {
        this(getConfig(args));
    }

    public Main(Config config)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ChooserFactory chooserFactory = new ChooserFactory(reader);
        archiver = new TarArchiver("archive", Path.of(System.getProperty("user.dir")), config.getArchiveDirectory());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> archiver.close()));

        findDuplicates = new FindDuplicates(config.getPrimaryDirectory(), config.getSecondaryDirectory(),
                chooserFactory.newInstance(config.chooser),
                archiver);
    }

    void start()
    {
        try
        {
            findDuplicates.find();
        }
        catch (IOException e)
        {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(IO_EXCEPTION);
        }
    }

    @Override
    public void close() throws IOException
    {
        archiver.close();
    }
}
