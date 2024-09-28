package dnt.photoorganiser.commands;

import dnt.common.Result;

import java.io.File;
import java.nio.file.Path;

public class MoveCommand extends Command
{
    private final File source;
    private final Path destination;

    public MoveCommand(File source, Path destination)
    {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public Result<Integer, String> execute()
    {
        return execute("mv " + source.getAbsolutePath() + " " + destination.toString());
    }
}