package dnt.photoorganiser.organiser;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.io.IOException;
import java.nio.file.Path;

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

    private void start()
    {
        new FileInfoWalker(Path.of("."));
    }

    public Main(String[] args)
    {
        this(getConfig(args));
    }

    public Main(Config config)
    {
        System.out.println(config);
    }
}