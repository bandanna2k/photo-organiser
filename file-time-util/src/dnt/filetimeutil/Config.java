package dnt.filetimeutil;

import com.beust.jcommander.Parameter;

import java.nio.file.Path;

public class Config
{
    public enum FileTimeType
    {
        CreationTime,
        ModifiedTime,
        LastAccessedTime,
    }

    @Parameter(names = {"-e", "--execute"})
    public boolean execute = false;

    @Parameter(names = {"--path"})
    public String path = System.getProperty("user.dir");

    @Parameter(names = {"--file-date-type"})
    public FileTimeType fileTimeType = FileTimeType.ModifiedTime;

    public Path path() { return Path.of(path); }
}