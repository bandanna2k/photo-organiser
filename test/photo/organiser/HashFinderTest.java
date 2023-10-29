package photo.organiser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static photo.organiser.HashFinder.Status.Complete;
import static photo.organiser.HashFinder.Status.Waiting;

public class HashFinderTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Path tempDir;

    private HashFinder hashFinder;

    @Before
    public void setUp() throws IOException
    {
        tempDir = getTemporaryDir();
        Files.writeString(new File(tempDir.toFile(), "file1").toPath(), "unique");
        Files.writeString(new File(tempDir.toFile(), "file2").toPath(), "same");
        Files.writeString(new File(tempDir.toFile(), "file3").toPath(), "same");
        hashFinder = new HashFinder(tempDir);
    }

    @Test
    public void testStatus()
    {
        assertThat(hashFinder.getStatus().status).isEqualTo(Waiting);
        hashFinder.start();
        assertThat(hashFinder.getStatus().status).isEqualTo(Complete);
    }

    @Test
    public void testCounts()
    {
        assertThat(hashFinder.getStatus().countOfFiles).isEqualTo(0);
        assertThat(hashFinder.getStatus().countOfHashesCollected).isEqualTo(0);
        assertThat(hashFinder.getStatus().countOfUniqueHashes).isEqualTo(0);
        hashFinder.start();
        assertThat(hashFinder.getStatus().countOfFiles).isEqualTo(3);
        assertThat(hashFinder.getStatus().countOfHashesCollected).isEqualTo(3);
        assertThat(hashFinder.getStatus().countOfUniqueHashes).isEqualTo(2);
    }

    private Path getTemporaryDir()
    {
        try
        {
            return temporaryFolder.newFolder().toPath();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}