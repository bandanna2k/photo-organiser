package dnt.photoorganiser.organiser;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static dnt.photoorganiser.testing.DirectoryAssertions.assertDirectory;

public class FileInfoWalkerDatesTest extends OrganiserTestBase
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException
    {
        temporaryFolder.newFolder("main1/sub1/sub1a");
        System.setProperty("user.dir", temporaryFolder.getRoot().toString());
    }

    @Test
    @Ignore // Creation date not accessible in Linux
    public void shouldUseCreationDate() throws IOException
    {
        FileInfoWalker walker = new FileInfoWalker(getConfig(Config.FileDateTimeMode.Creation));
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file1"), CONTENT, JANUARY, FEBRUARY, MARCH);

        walker.walkDirectory();

        assertDirectory(temporaryFolder.getRoot().toPath(),
                "2023/January/file1");
    }

    @Test
    public void shouldUseModifiedDate() throws IOException
    {
        FileInfoWalker walker = new FileInfoWalker(getConfig(Config.FileDateTimeMode.Modified));
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file1"), CONTENT, JANUARY, FEBRUARY, MARCH);

        walker.walkDirectory();

        assertDirectory(temporaryFolder.getRoot().toPath(),
                "2023/February/file1");
    }

    @Test
    public void shouldUseLastAccessedDate() throws IOException
    {
        FileInfoWalker walker = new FileInfoWalker(getConfig(Config.FileDateTimeMode.LastAccessed));
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file1"), CONTENT, JANUARY, FEBRUARY, MARCH);

        walker.walkDirectory();

        assertDirectory(temporaryFolder.getRoot().toPath(),
                "2023/March/file1");
    }
}