package dnt.photoorganiser.organiser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

public class FileInfoWalkerTest extends FileTestBase
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException
    {
        temporaryFolder.newFolder("main1/sub1/sub1a");
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file1a"), CONTENT, MORNING, NOON, NIGHT);
    }

    @Test
    public void shouldWalkDirectory() throws IOException
    {
        FileInfoWalker walker = new FileInfoWalker(temporaryFolder.getRoot().toPath());
        walker.walkDirectory();
    }
}