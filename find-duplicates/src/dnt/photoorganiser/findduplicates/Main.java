package dnt.photoorganiser.findduplicates;

import com.beust.jcommander.JCommander;
import dnt.photoorganiser.findduplicates.archiver.TarArchiver;
import dnt.photoorganiser.findduplicates.choosers.AlphabeticalPathChooser;

import java.io.Closeable;
import java.io.IOException;

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
        return config;
    }

    public Main(String[] args)
    {
        this(getConfig(args));
    }

    public Main(Config config)
    {
        archiver = new TarArchiver("archive", config.getSecondaryDirectory(), config.getArchiveDirectory());
        findDuplicates = new FindDuplicates(config.getPrimaryDirectory(), config.getSecondaryDirectory(),
                new AlphabeticalPathChooser(),
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
