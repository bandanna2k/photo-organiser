package dnt.photoorganiser.commands;

import dnt.common.Result;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static dnt.common.Result.failure;
import static dnt.common.Result.success;

public abstract class Command
{
    abstract Result<Integer, String> execute();

    @Deprecated
    protected Result<Integer, String> execute(String command)
    {
        return execute(command, Path.of(System.getProperty("user.dir")));
    }

    protected Result<Integer, String> execute(String[] command)
    {
        Result<Integer, String> result = execute(command, Path.of(System.getProperty("user.dir")));
        return result.fold(returnCode -> {
            if(returnCode == 0) return success(returnCode);
            return failure("Non zero return code. " + Arrays.toString(command));
        }, Result::failure);
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
