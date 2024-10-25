package dnt.photoorganiser.imagecompressor;

import com.beust.jcommander.Parameter;

import java.nio.file.Path;
import java.util.List;

public class Config
{
    @Parameter(names = "--extensions")
    public List<String> extensions = List.of("jpg", "jpeg", "png");

    @Parameter(names = "--postfix")
    public String postfix = "min";

    @Override
    public String toString()
    {
        return "Config{" +
                "extensions=" + extensions +
                ", postfix='" + postfix + '\'' +
                '}';
    }

    //
//    @Parameter(names = "--load-hash-cache", converter = BooleanConverter.class)
//    public String shouldLoadHashCache = "true";
//
//    public enum ChooserType
//    {
//        AutoChooser,
//        CommandLineChooser,
//        SmartChooser
//    }
//
//    public Path getSecondaryDirectory()
//    {
//        return directories.get(1);
//    }
//
//    public Path getArchiveDirectory()
//    {
//        return directories.get(2);
//    }
//
//    public Path getPrimaryDirectory()
//    {
//        return directories.getFirst();
//    }
//
//    public boolean shouldLoadHashCache()
//    {
//        return Boolean.parseBoolean(shouldLoadHashCache);
//    }
//
//    @Override
//    public String toString()
//    {
//        return "dnt.photoorganiser.imagecompressor.Config{" +
//                "directories=" + directories +
//                ", chooser=" + chooser +
//                ", maxFilesInATar=" + maxFilesInATar +
//                ", allFiles=" + allFiles +
//                ", extensions=" + extensions +
//                ", shouldLoadHashCache='" + shouldLoadHashCache + '\'' +
//                '}';
//    }
//
//    public static class FindDuplicatesDirectoriesValidator implements IParameterValidator
//    {
//        @Override
//        public void validate(String name, String value) throws ParameterException
//        {
//            Path path = Path.of(value);
//            if (!path.toFile().exists())
//            {
//                throw new ParameterException("Path not found: " + value);
//            }
//        }
//    }
//
//    private static class BooleanConverter implements IStringConverter<String>
//    {
//        @Override
//        public String convert(String value)
//        {
//            return String.valueOf(Boolean.parseBoolean(value));
//        }
//    }
}