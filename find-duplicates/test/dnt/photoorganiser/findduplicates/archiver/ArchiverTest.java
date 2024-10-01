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
import java.util.ArrayList;

import static dnt.photoorganiser.testing.DirectoryAssertions.assertDirectory;
import static dnt.photoorganiser.testing.DirectoryAssertions.assertDirectoryUsingRegex;
import static org.assertj.core.api.Assertions.assertThat;

public class ArchiverTest implements AutoCloseable
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private TarArchiver tarArchiver;
    private Path root;
    private Path archiveDirectory;

    @Before
    public void setUp() throws IOException
    {
        root = temporaryFolder.getRoot().toPath();

        archiveDirectory = temporaryFolder.newFolder("Archive").toPath();
        tarArchiver = new TarArchiver("test", temporaryFolder.getRoot().toPath(), archiveDirectory);
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

    @Test
    public void shouldNotAutoArchiveFiles() throws IOException
    {
        ArrayList<Object> addedFiles = new ArrayList<>();
        for (int i = 1; i < 10; i++)
        {
            String filename = "file" + i;
            addedFiles.add(filename);
            createFileAndArchive(filename, "content" + i);
        }

        assertDirectory(root, addedFiles.toArray(String[]::new));
    }

    @Test
    public void shouldAutoArchiveFiles() throws IOException
    {
        for (int i = 1; i < 11; i++)
        {
            String filename = "file" + i;
            createFileAndArchive(filename, "content" + i);
        }
        assertDirectoryUsingRegex(root,
                ".*Archive\\/test.*tar.gz"
        );
    }

    @Test
    public void shouldAutoArchiveOnAutoClose() throws IOException
    {
        Path filePath = new File(temporaryFolder.getRoot(), "file").toPath();
        Files.writeString(filePath, "content");

        try(TarArchiver archiver = new TarArchiver("autoClose", root, archiveDirectory))
        {
            archiver.add(filePath);
        }

        assertDirectoryUsingRegex(root,
                ".*Archive\\/autoClose.*tar.gz"
        );
    }

    @Test
    public void shouldArchiveEveryXFiles() throws IOException
    {
        try (TarArchiver archiver = new TarArchiver("xFiles", temporaryFolder.getRoot().toPath(), archiveDirectory, 1))
        {
            createFileAndArchive("mulder", "fox", archiver);
            createFileAndArchive("scully", "dana", archiver);
        }
        assertDirectoryUsingRegex(root,
                ".*Archive\\/xFiles.*.0001.*.tar.gz",
                ".*Archive\\/xFiles.*.0002.*.tar.gz"
        );
    }

    @Test
    public void shouldClearListAfterArchiving() throws IOException
    {
        try (TarArchiver archiver = new TarArchiver("xFiles", temporaryFolder.getRoot().toPath(), archiveDirectory, 2))
        {
            createFileAndArchive("mulder", "fox", archiver);
            createFileAndArchive("scully", "dana", archiver);
            createFileAndArchive("cancerman", "malborough", archiver);
            createFileAndArchive("eugene", "victor", archiver);
        }
        assertDirectoryUsingRegex(root,
                ".*Archive\\/xFiles.*.0001.*.tar.gz",
                ".*Archive\\/xFiles.*.0002.*.tar.gz"
        );
    }

    private void createFileAndArchive(String name, String content) throws IOException
    {
        createFileAndArchive(name, content, tarArchiver);
    }
    private void createFileAndArchive(String name, String content, Archiver archiver) throws IOException
    {
        Path filePath = new File(temporaryFolder.getRoot(), name).toPath();
        Files.writeString(filePath, content);
        archiver.add(root.relativize(filePath));
    }

    @Override
    public void close() throws Exception
    {
        tarArchiver.close();
    }
}