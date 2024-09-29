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
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class FindDuplicatesTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private FindDuplicates findDuplicates;
    private Path source;
    private DuplicateCollector collector;

    @Before
    public void setUp() throws IOException
    {
//        destination = temporaryFolder.newFolder("Album").toPath();
        source = temporaryFolder.newFolder("Pit").toPath();
  //      archive = temporaryFolder.newFolder("Archive").toPath();

        findDuplicates = new FindDuplicates(source);
        collector = new DuplicateCollector();
    }

    @Test
    public void shouldFindDuplicates() throws IOException
    {
        Files.writeString(new File(source.toFile(), "file1").toPath(), "unique");
        Files.writeString(new File(source.toFile(), "file2").toPath(), "same");
        Files.writeString(new File(source.toFile(), "file3").toPath(), "same");

        findDuplicatesAndCollect();

        assertThat(collector.sizeHashToFiles.size()).isEqualTo(1);

        List<Path> values = collector.sizeHashToFiles.values().stream().findFirst().get();
        assertThat(values.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindNoDuplicates() throws IOException
    {
        Files.writeString(new File(source.toFile(), "file1").toPath(), "unique");
        Files.writeString(new File(source.toFile(), "file2").toPath(), "same");
        Files.writeString(new File(source.toFile(), "file3").toPath(), "SAME");

        findDuplicatesAndCollect();

        assertThat(collector.sizeHashToFiles.size()).isEqualTo(0);
    }

    private void findDuplicatesAndCollect() throws IOException
    {
        findDuplicates.walkSource();
        findDuplicates.forEachDuplicate(collector);
    }

    @Test
    public void duplicateIsInTheDestinationDirectory()
    {
fail();
    }

    private static class DuplicateCollector implements BiConsumer<SizeHash, List<Path>>
    {
        final Map<SizeHash, List<Path>> sizeHashToFiles = new HashMap<>();

        @Override
        public void accept(SizeHash sizeHash, List<Path> paths)
        {
            sizeHashToFiles.put(sizeHash, paths);
        }
    }
}