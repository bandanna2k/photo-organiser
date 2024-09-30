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
    public ChooserType chooser = ChooserType.CommandLineChooser;
    public enum ChooserType
    {
        AutoChooser,
        CommandLineChooser
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

    public static class ListConverter implements IStringConverter<List<Path>>
    {
        @Override
        public List<Path> convert(String files)
        {
            System.out.println("Converting");
            String [] paths = files.split(" ");
            List<Path> pathList = new ArrayList<>();
            for(String path : paths)
            {
                pathList.add(Path.of(path));
            }
            if(pathList.size() != 3)
            {
                throw new ParameterException("3 Directories must be provided.");
            }
            return List.of();
        }
    }
}