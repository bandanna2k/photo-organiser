package dnt.photoorganiser.organiser;

import com.beust.jcommander.JCommander;

import java.io.IOException;

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

    private final Config config;

    private void start() throws IOException
    {
        new FileInfoWalker(config).walkDirectory();
    }

    public Main(String[] args)
    {
        this(getConfig(args));
    }

    public Main(Config config)
    {
        this.config = config;
    }
}