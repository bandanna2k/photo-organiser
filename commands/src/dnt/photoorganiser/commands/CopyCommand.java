package dnt.photoorganiser.commands;

import dnt.common.Result;

import java.nio.file.Path;
import java.util.Arrays;

public class CopyCommand extends Command
{
    private final Path source;
    private final Path destination;

    public CopyCommand(Path source, Path destination)
    {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public Result<Integer, String> execute()
    {
        return execute(getCommand());
    }

    private String[] getCommand()
    {
        return new String[]{
                "cp", "--preserve", source.toString(), destination.toString()
        };
    }

    @Override
    public String toString()
    {
        return Arrays.toString(getCommand());
    }
}