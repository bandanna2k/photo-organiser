package dnt.photoorganiser.findduplicates;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class FileSizeHashCollectorTest extends FileHashGeneratorBase
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Path source;

    @Before
    public void setUp() throws IOException
    {
//        destination = temporaryFolder.newFolder("Album").toPath();
        source = temporaryFolder.newFolder("Pit").toPath();
  //      archive = temporaryFolder.newFolder("Archive").toPath();

        fileSizeHashCollector = new FileSizeHashCollector(source);
    }

    @Test
    public void shouldFindHashes() throws IOException
    {
        Files.writeString(new File(source.toFile(), "file1").toPath(), "unique");
        Files.writeString(new File(source.toFile(), "file2").toPath(), "same");
        Files.writeString(new File(source.toFile(), "file3").toPath(), "same");

        findDuplicatesAndCollect();

        assertThat(collector.sizeHashToFiles.size()).isEqualTo(2);
        {
            List<Path> values = collector.sizeHashToFiles.values().stream().filter(l -> l.size() == 1).findFirst().get();
            assertThat(values.size()).isEqualTo(1);
        }
        {
            List<Path> values = collector.sizeHashToFiles.values().stream().filter(l -> l.size() == 2).findFirst().get();
            assertThat(values.size()).isEqualTo(2);
        }
    }

    @Test
    public void shouldFindNoDuplicates() throws IOException
    {
        Files.writeString(new File(source.toFile(), "file1").toPath(), "unique");
        Files.writeString(new File(source.toFile(), "file2").toPath(), "same");
        Files.writeString(new File(source.toFile(), "file3").toPath(), "SAME");

        findDuplicatesAndCollect();

        assertThat(collector.sizeHashToFiles.size()).isEqualTo(3);
    }

    @Test
    public void duplicateIsInTheDestinationDirectory()
    {
fail();
    }
}