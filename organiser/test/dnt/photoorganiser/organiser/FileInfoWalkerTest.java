package dnt.photoorganiser.organiser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static dnt.photoorganiser.testing.DirectoryAssertions.assertDirectory;

public class FileInfoWalkerTest extends OrganiserTestBase
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private FileInfoWalker walker;

    @Before
    public void setUp() throws IOException
    {
        temporaryFolder.newFolder("main1/sub1/sub1a");
        System.setProperty("user.dir", temporaryFolder.getRoot().toString());

        walker = new FileInfoWalker(getConfig());
    }

    @Test
    public void shouldWalkDirectory() throws IOException
    {
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file1a"), CONTENT, MORNING, NOON, NIGHT);

        walker.walkDirectory();

        assertDirectory(temporaryFolder.getRoot().toPath(),
                "2024/October/file1a");
    }

    @Test
    public void shouldMoveFileWithSpacesInName() throws IOException
    {
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file with spaces"), CONTENT, MORNING, NOON, NIGHT);

        walker.walkDirectory();

        assertDirectory(temporaryFolder.getRoot().toPath(),
                "2024/October/file with spaces");
    }

    @Test
    public void shouldOnlyMoveRequestedExtensionsCaseSensitive() throws IOException
    {
        Config config = getConfig();
        config.extensions = List.of("one", "TWO", "five");
        FileInfoWalker newWalker = new FileInfoWalker(config);

        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file.one"), CONTENT, MORNING, NOON, NIGHT);
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file.two"), CONTENT, MORNING, NOON, NIGHT);
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file.three"), CONTENT, MORNING, NOON, NIGHT);
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file.four"), CONTENT, MORNING, NOON, NIGHT);
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file.FIVE"), CONTENT, MORNING, NOON, NIGHT);

        newWalker.walkDirectory();

        assertDirectory(temporaryFolder.getRoot().toPath(),
                "2024/October/file.one",
                "main1/sub1/file.two",
                "main1/sub1/file.three",
                "main1/sub1/file.four",
                "main1/sub1/file.FIVE"
        );
    }
}