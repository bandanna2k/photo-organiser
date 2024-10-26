package dnt.photoorganiser.commands;

import dnt.common.Result;

import java.nio.file.Path;
import java.util.Arrays;

public class DeleteCommand extends Command
{
    private final Path file;

    public DeleteCommand(Path file)
    {
        this.file = file;
    }

    @Override
    public Result<Integer, String> execute()
    {
        return execute(getCommand());
    }

    private String[] getCommand()
    {
        return new String[]{
                "rm", file.toString()
        };
    }

    @Override
    public String toString()
    {
        return Arrays.toString(getCommand());
    }
}