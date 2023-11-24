package photo.organiser;

import photo.organiser.common.Result;

import java.io.File;
import java.nio.file.Path;

public class Config
{
    private final Path targetDirectory;
    public final Path triageDirectory;
    public final Path archiveDirectory;

    public Config(String[] args)
    {
        try
        {
            triageDirectory = Path.of(args[0]);
            targetDirectory = Path.of(args[1]);
            archiveDirectory = Path.of(args[2]);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public Result<Void, String> validate()
    {
        Result<Void, String> photoResult = validateDirectory(targetDirectory);
        if(photoResult.isFailure()) return photoResult;

        Result<Void, String> archiveResult = validateDirectory(archiveDirectory);
        if(archiveResult.isFailure()) return archiveResult;

        Result<Void, String> awaitingResult = validateDirectory(triageDirectory);
        if(awaitingResult.isFailure()) return awaitingResult;

        return Result.success();
    }

    private Result<Void, String> validateDirectory(Path directory)
    {
        File childDir = directory.toFile();
        if(!childDir.exists())
        {
            return Result.failure("Directory does not exist. " + childDir.getAbsolutePath());
        }
        if(!childDir.isDirectory())
        {
            return Result.failure("Given directory is not a directory. " + childDir.getAbsolutePath());
        }
        return Result.success(null);
    }

    @Override
    public String toString()
    {
        return "Config{" +
                "targetDirectory=" + targetDirectory +
                ", triageDirectory=" + triageDirectory +
                ", archiveDirectory=" + archiveDirectory +
                '}';
    }
}
