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
import java.util.function.Consumer;

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

        assertThat(collector.sizeToDuplicates.size()).isEqualTo(1);

        List<FindDuplicates.FileData> values = collector.sizeToDuplicates.values().stream().findFirst().get();
        assertThat(values.size()).isEqualTo(2);
    }

    @Test
    public void shouldFindDuplicatesNeedingHash() throws IOException
    {
        Files.writeString(new File(source.toFile(), "file1").toPath(), "unique");
        Files.writeString(new File(source.toFile(), "file2").toPath(), "same");
        Files.writeString(new File(source.toFile(), "file3").toPath(), "SAME");

        findDuplicatesAndCollect();

        assertThat(collector.sizeToDuplicates.size()).isEqualTo(1);

        List<FindDuplicates.FileData> values = collector.sizeToDuplicates.values().stream().findFirst().get();
        assertThat(values.size()).isEqualTo(2);
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

    private static class DuplicateCollector implements BiConsumer<Long, List<FindDuplicates.FileData>>
    {
        final Map<Long, List<FindDuplicates.FileData>> sizeToDuplicates = new HashMap<>();

        @Override
        public void accept(Long size, List<FindDuplicates.FileData> listOfDuplicates)
        {
            sizeToDuplicates.put(size, listOfDuplicates);
        }
    }
}