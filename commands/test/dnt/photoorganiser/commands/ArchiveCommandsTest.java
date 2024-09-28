package dnt.photoorganiser.commands;

import dnt.common.Result;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

public class ArchiveCommandsTest
{
    private static final String IT_WAS___ = "It was the best of times, it was the worst of times.";

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private final Random random = new Random();

    private Path root;

    @Before
    public void setUp() throws Exception
    {
        temporaryFolder.create();

        root = temporaryFolder.getRoot().toPath();
    }

    @After
    public void tearDown()
    {
        temporaryFolder.delete();
    }

    @Test
    public void shouldTar() throws IOException
    {
        Path dir1 = temporaryFolder.newFolder().toPath();
        Path file1 = Files.writeString(dir1.resolve("dickens.txt"),
                IT_WAS___,
                CREATE_NEW);

        String archiveFilename = "tar" + random.nextLong() + ".tar.gz";
        Path destination = temporaryFolder.newFile(archiveFilename).toPath();
        Result<Integer, String> resultTar = new ArchiveCommands.Tar(root, destination)
                .withSource(root.relativize(dir1))
                .execute();
        assertThat(resultTar.success()).isEqualTo(0);
        assertTrue(destination.toFile().exists());
        assertDirectory(root, archiveFilename);
    }

    @Test
    public void shouldTarAndUntar() throws IOException
    {
        Path file1 = temporaryFolder.newFile("tarAndUntar.log").toPath();
        Path dir1 = temporaryFolder.newFolder("subFolder").toPath();
        Path file2 = Files.writeString(dir1.resolve("tarAndUntar.txt"), "logging", CREATE_NEW);

        String archiveFilename = random.nextLong() + ".tar.gz";
        Path archiveFile = temporaryFolder.newFile(archiveFilename).toPath();
        Result<Integer, String> resultTar = new ArchiveCommands.Tar(root, archiveFile)
                .withSource(root.relativize(file1), root.relativize(file2))
                .execute();
        assertThat(resultTar.success()).isEqualTo(0);

        assertDirectory(
                root,
                archiveFilename
        );

        Path extractLocation = temporaryFolder.newFolder("extractLocation").toPath();
        Result<Integer, String> resultUntar = new ArchiveCommands.Untar(extractLocation, archiveFile).execute();
        assertThat(resultUntar.success()).isEqualTo(0);

        assertDirectory(
                root,
                archiveFilename,
                "extractLocation/tarAndUntar.log",
                "extractLocation/subFolder/tarAndUntar.txt"
        );
    }

    private void assertDirectory(Path root, String... archiveFilenames) throws IOException
    {
        List<Path> files = new ArrayList<>();
        List<Path> expectedFiles = new ArrayList<>(Arrays.stream(archiveFilenames)
                .map(root::resolve).collect(Collectors.toList()));
        Files.walkFileTree(root, new SimpleFileVisitor<>()
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

    @Test
    public void shouldTarRelativeFilenames()
    {
fail();
    }

    @Test
    public void shouldTarADirectory()
    {
        fail();
    }
}