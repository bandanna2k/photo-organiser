package dnt.photoorganiser.findduplicates;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.nio.file.Path;
import java.util.List;

class Config
{
    //        @Parameter(
//                required = true,
//                description = "Primary directory. Any files in here are treated as 'source of truth'.",
//                converter = PathConverter.class
//        )
//        public Path primaryDirectory;
//
//        @Parameter(
//                required = true,
//                description = "Secondary directory.",
//                converter = PathConverter.class
//        )
//        public Path secondaryDirectory;
//
//        @Parameter(
//                required = true,
//                description = "Archive directory.",
//                converter = PathConverter.class
//        )
//        public Path archiveDirectory;
    @Parameter(
            required = true,
            validateWith = FindDuplicatesDirectoriesValidator.class
    )
    public List<String> directories;

    public Path getSecondaryDirectory()
    {
        return Path.of(directories.get(1));
    }

    public Path getArchiveDirectory()
    {
        return Path.of(directories.get(2));
    }

    public Path getPrimaryDirectory()
    {
        return Path.of(directories.getFirst());
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