package dnt.photoorganiser.findduplicates;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static dnt.photoorganiser.testing.DirectoryAssertions.assertDirectoryUsingRegex;

public class MainTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Path root;
    private Path primaryDirectory;
    private Path secondaryDirectory;
    private Path archiveDirectory;

    @Before
    public void setUp() throws Exception
    {
        root = temporaryFolder.getRoot().toPath();
        primaryDirectory = temporaryFolder.newFolder("Album").toPath();
        secondaryDirectory = temporaryFolder.newFolder("Pit").toPath();
        archiveDirectory = temporaryFolder.newFolder("Archive").toPath();
    }

    @Test
    public void shouldFindDuplicatesAndArchive() throws IOException
    {
        Files.writeString(new File(secondaryDirectory.toFile(), "file1").toPath(), "unique");
        Files.writeString(new File(secondaryDirectory.toFile(), "file2").toPath(), "same");
        Files.writeString(new File(secondaryDirectory.toFile(), "file3").toPath(), "same");

        String[] args = {
                root + "/Album",
                root + "/Pit",
                root + "/Archive",
                "--chooser", "AutoChooser"
        };
        try (Main app = new Main(args))
        {
            app.start();
        }
        assertDirectoryUsingRegex(root,
                ".*file1",
                ".*file2",
                ".*archive.*0001.tar.gz");
    }

    @Test
    @Ignore // Manual test
    public void shouldRunFromCommandLine() throws IOException
    {
        Files.writeString(new File(secondaryDirectory.toFile(), "file1").toPath(), "unique");
        Files.writeString(new File(secondaryDirectory.toFile(), "file2").toPath(), "same");
        Files.writeString(new File(secondaryDirectory.toFile(), "file3").toPath(), "same");

        String[] args = {
                root + "/Album",
                root + "/Pit",
                root + "/Archive",
        };
        try (Main app = new Main(args))
        {
            app.start();
        }
    }
}
