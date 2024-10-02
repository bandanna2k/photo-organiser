package dnt.photoorganiser.findduplicates;

import com.beust.jcommander.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Config
{
    @Parameter(
            required = true,
            validateWith = FindDuplicatesDirectoriesValidator.class
    )
    public List<Path> directories;

    @Parameter(names = {"--chooser"})
    public ChooserType chooser = ChooserType.SmartChooser;

    @Parameter(names = {"--max-tarred-files"})
    public int maxFilesInATar = 30;

    @Parameter(names = "--all-files")
    public boolean allFiles = false;

    @Parameter(names = "--extensions")
    public List<String> extensions = List.of("jpg", "jpeg", "png");

    public enum ChooserType
    {
        AutoChooser,
        CommandLineChooser,
        SmartChooser
    }

    public Path getSecondaryDirectory()
    {
        return directories.get(1);
    }

    public Path getArchiveDirectory()
    {
        return directories.get(2);
    }

    public Path getPrimaryDirectory()
    {
        return directories.getFirst();
    }

    @Override
    public String toString()
    {
        return "Config{" +
                "directories=" + directories +
                ", chooser=" + chooser +
                ", maxFilesInATar=" + maxFilesInATar +
                ", allFiles=" + allFiles +
                ", extensions=" + extensions +
                '}';
    }

    public static class FindDuplicatesDirectoriesValidator implements IParameterValidator
    {
        @Override
        public void validate(String name, String value) throws ParameterException
        {
            Path path = Path.of(value);
            if (!path.toFile().exists())
            {
                throw new ParameterException("Path not found: " + value);
            }
        }
    }
}