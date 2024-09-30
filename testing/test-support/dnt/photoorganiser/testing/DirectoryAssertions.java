package dnt.photoorganiser.testing;

import org.assertj.core.api.SoftAssertions;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class DirectoryAssertions
{
    public static void assertDirectory(Path path, String... archiveFilenames) throws IOException
    {
        List<Path> files = new ArrayList<>();
        List<Path> expectedFiles = new ArrayList<>(Arrays.stream(archiveFilenames)
                .map(path::resolve).collect(Collectors.toList()));
        Files.walkFileTree(path, new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
            {
                files.add(file);
                return super.visitFile(file, attrs);
            }
        });
        SoftAssertions softly = new SoftAssertions();

        Iterator<Path> iterator = expectedFiles.iterator();
        while(iterator.hasNext())
        {
            Path expectedFile = iterator.next();
            softly.assertThat(files.remove(expectedFile))
                    .describedAs("File not found. " + expectedFile)
                    .isTrue();
            iterator.remove();
        }
        softly.assertThat(expectedFiles)
                .describedAs("Expected files not accounted for: \n" + expectedFiles.stream().map(Path::toString).collect(Collectors.joining("\n")))
                .isEmpty();
        softly.assertThat(files)
                .describedAs("Other files found: \n" + files.stream().map(Path::toString).collect(Collectors.joining("\n")))
                .isEmpty();
        softly.assertAll();
    }
}
