package dnt.photoorganiser.commands;

import dnt.common.Result;

import java.io.File;
import java.nio.file.Path;

public class MoveCommand extends Command
{
    private final Path source;
    private final Path destination;

    public MoveCommand(Path source, Path destination)
    {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public Result<Integer, String> execute()
    {
        return execute("mv " + source + " " + destination);
    }
}