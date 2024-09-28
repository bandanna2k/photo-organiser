package dnt.photoorganiser.commands;

import dnt.common.Result;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ArchiveCommands extends Command
{
    public static class Tar extends Command
    {
        private final List<Path> sourceDirectories = new ArrayList<>();
        private final List<File> sourceFiles = new ArrayList<>();
        private final Path destination;
        private final Path workingDirectory;

        public Tar(Path workingDirectory, Path destination)
        {
            assert destination.toString().endsWith(".tar.gz");
            this.workingDirectory = workingDirectory;
            this.destination = destination;
        }

        @Override
        public Result<Integer, String> execute()
        {
            String files = sourceFiles.stream().map(File::getAbsolutePath).collect(Collectors.joining(" "));
            String directories = sourceDirectories.stream().map(Path::toString).collect(Collectors.joining(" "));
            String command = "tar czvf " + destination.toString() +
                    " " + files +
                    " " + directories +
                    " --remove-files";
//            System.out.println(command);
            return execute(command, workingDirectory);
        }

        public Command withSource(Path... directories)
        {
            sourceDirectories.addAll(Arrays.asList(directories));
            return this;
        }

        public Command withSource(File... files)
        {
            sourceFiles.addAll(Arrays.asList(files));
            return this;
        }
    }

    public static class Untar extends Command
    {
        private final Path tarFile;
        private final Path workingDirectory;

        public Untar(Path workingDirectory, Path tarFile)
        {
            this.workingDirectory = workingDirectory;
            this.tarFile = tarFile;
        }

        @Override
        public Result<Integer, String> execute()
        {
            return execute("tar xvf " + tarFile.toString(), workingDirectory);
        }
    }
}