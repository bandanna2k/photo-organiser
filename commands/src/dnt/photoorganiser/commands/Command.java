package dnt.photoorganiser.commands;

import dnt.common.Result;

import java.io.IOException;
import java.nio.file.Path;

import static dnt.common.Result.failure;
import static dnt.common.Result.success;

public abstract class Command
{
    abstract Result<Integer, String> execute();

    protected Result<Integer, String> execute(String command)
    {
        return execute(command, Path.of(System.getProperty("user.dir")));
    }

    protected Result<Integer, String> execute(String[] command)
    {
        return execute(command, Path.of(System.getProperty("user.dir")));
    }

    @Deprecated
    protected Result<Integer, String> execute(String command, Path workingDirectory)
    {
        Process p = null;
        try
        {
            p = Runtime.getRuntime().exec(command, null, workingDirectory.toFile());
            return success(p.waitFor());
        }
        catch (IOException | InterruptedException e)
        {
            return failure(e.getMessage());
        }
    }

    protected Result<Integer, String> execute(String[] command, Path workingDirectory)
    {
        Process p = null;
        try
        {
            p = Runtime.getRuntime().exec(command, null, workingDirectory.toFile());
            return success(p.waitFor());
        }
        catch (IOException | InterruptedException e)
        {
            return failure(e.getMessage());
        }
    }
}
