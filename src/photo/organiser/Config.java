package photo.organiser;

import photo.organiser.common.Result;

import java.io.File;
import java.nio.file.Path;

public class Config
{
    private Path dir;

    public Config(String[] args)
    {
        try
        {
            dir = Path.of(args[0]);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public Result<Void, String> validate()
    {
        Result<Void, String> photoResult = validateChildDirectory("Photos");
        if(photoResult.isSuccess()) return photoResult;

        Result<Void, String> duplicates = validateChildDirectory("Duplicates");
        if(duplicates.isSuccess()) return duplicates;

        Result<Void, String> awaitingResult = validateChildDirectory("Awaiting");
        if(awaitingResult.isSuccess()) return awaitingResult;

        return Result.success();
    }

    private Result<Void, String> validateChildDirectory(String child)
    {
        File childDir = new File(dir.toFile(), child);
        if(!childDir.exists())
        {
            return Result.failure("Directory does not exist. " + childDir);
        }
        if(!childDir.isDirectory())
        {
            return Result.failure("Given directory is not a directory. " + childDir);
        }
        return Result.success(null);
    }

    @Override
    public String toString()
    {
        return "Config{" +
                "dir=" + dir +
                '}';
    }
}
