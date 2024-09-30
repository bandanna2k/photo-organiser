package dnt.photoorganiser.findduplicates.archiver;

import dnt.photoorganiser.findduplicates.TestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static dnt.photoorganiser.testing.DirectoryAssertions.assertDirectory;
import static dnt.photoorganiser.testing.DirectoryAssertions.assertDirectoryUsingRegex;
import static org.assertj.core.api.Assertions.assertThat;

public class ArchiverTest implements AutoCloseable
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private TarArchiver tarArchiver;
    private Path root;

    @Before
    public void setUp() throws IOException
    {
        root = temporaryFolder.getRoot().toPath();

        Path directory = temporaryFolder.newFolder("Archive").toPath();
        tarArchiver = new TarArchiver("test", temporaryFolder.getRoot().toPath(), directory);
    }

    @Test
    public void shouldArchiveFiles() throws IOException
    {
        createFileAndArchive("file1", "nonsense");
        createFileAndArchive("file2", "junk");
        createFileAndArchive("file3", "blah");

        tarArchiver.archive();

        assertDirectoryUsingRegex(root,
                ".*Archive\\/test.*tar.gz"
        );
    }

    private void createFileAndArchive(String name, String content) throws IOException
    {
        Path filePath = new File(temporaryFolder.getRoot(), name).toPath();
        Files.writeString(filePath, content);
        tarArchiver.add(root.relativize(filePath));
    }

    @Override
    public void close() throws Exception
    {
        tarArchiver.close();
    }
}