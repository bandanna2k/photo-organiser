package dnt.photoorganiser.findduplicates.filesizehashcollector;

import dnt.photoorganiser.findduplicates.FileSizeHashTestCollector;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    @Test
    public void shouldSaveAndLoad() throws IOException
    {
        File file = new File(source.toFile(), "saveAndLoad.json");
        {
            FileSizeHashCollector collector = new FileSizeHashCollector(source, true, emptySet());
            FileSizeHashTestCollector testCollector = new FileSizeHashTestCollector();

            for (int i = 0; i < 9; i++)
                Files.writeString(new File(source.toFile(), "fileA" + i + ".txt").toPath(), "fileA content");
            for (int i = 0; i < 8; i++)
                Files.writeString(new File(source.toFile(), "fileB" + i + ".txt").toPath(), "fileB content");
            for (int i = 0; i < 7; i++)
                Files.writeString(new File(source.toFile(), "fileC" + i + ".txt").toPath(), "fileC content");

            findDuplicatesAndCollect(collector, testCollector);

            assertThat(testCollector.sizeHashToFiles.size()).isEqualTo(3);
            assertThat(testCollector.paths.size()).isEqualTo(24);

            collector.save(file);
        }
        {
            FileSizeHashCollector collector = new FileSizeHashCollector(source, true, emptySet());
            FileSizeHashTestCollector testCollector = new FileSizeHashTestCollector();
            collector.load(file);

            collector.forEachSizeHash(testCollector);

            assertThat(testCollector.sizeHashToFiles.size()).isEqualTo(3);
            assertThat(testCollector.paths.size()).isEqualTo(24);
        }
    }
}