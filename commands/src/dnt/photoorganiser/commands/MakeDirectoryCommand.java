package dnt.photoorganiser.commands;

import dnt.common.Result;

import java.nio.file.Path;
import java.util.Arrays;

public class MakeDirectoryCommand extends Command
{
    private final Path path;

    public MakeDirectoryCommand(Path path)
    {
        this.path = path;
    }

    @Override
    public Result<Integer, String> execute()
    {
        return execute(getCommand());
    }

    private String[] getCommand()
    {
        return new String[]{
                "mkdir", "-p", path.toString()
        };
    }

    @Override
    public String toString()
    {
        return Arrays.toString(getCommand());
    }
}