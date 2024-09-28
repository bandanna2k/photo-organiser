package dnt.photoorganiser.commands;

import dnt.common.Result;

import java.io.File;
import java.nio.file.Path;

public class TouchCommand extends Command
{
    private final Path destination;

    public TouchCommand(Path destination)
    {
        this.destination = destination;
    }

    @Override
    public Result<Integer, String> execute()
    {
        return execute("touch " + destination.toString());
    }
}