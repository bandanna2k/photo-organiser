package dnt.photoorganiser.commands;

import dnt.common.Result;

import java.nio.file.Path;

public class DeleteEmptyDirectoriesCommand extends Command
{
    private final Path workingDirectory;

    public DeleteEmptyDirectoriesCommand(Path workingDirectory)
    {
        this.workingDirectory = workingDirectory;
    }

    @Override
    public Result<Integer, String> execute()
    {
        return execute("find . -type d -empty -delete", workingDirectory);
    }
}