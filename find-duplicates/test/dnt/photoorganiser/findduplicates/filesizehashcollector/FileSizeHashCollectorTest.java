package dnt.photoorganiser.findduplicates.filesizehashcollector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

public class FileSizeHashCollectorTest extends FileSizeHashCollectorTestBase
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Path source;

    @Before
    public void setUp() throws IOException
    {
        source = temporaryFolder.newFolder("Pit").toPath();

        fileSizeHashCollector = new FileSizeHashCollector(source, true, emptySet());
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
    public void shouldSortBySize() throws IOException
    {
        for (int i = 0; i < 9; i++) Files.writeString(new File(source.toFile(), "fileA" + i).toPath(), "fileA content");
        for (int i = 0; i < 8; i++) Files.writeString(new File(source.toFile(), "fileB" + i).toPath(), "fileB content");
        for (int i = 0; i < 7; i++) Files.writeString(new File(source.toFile(), "fileC" + i).toPath(), "fileC content");

        findDuplicatesAndCollect();

        List<List<Path>> listOfPaths = new ArrayList<>();
        fileSizeHashCollector.forEachSizeHash(((sizeHash, paths) -> listOfPaths.add(paths)));
        assertThat(listOfPaths.get(0).size()).isEqualTo(9);
        assertThat(listOfPaths.get(1).size()).isEqualTo(8);
        assertThat(listOfPaths.get(2).size()).isEqualTo(7);
    }
}