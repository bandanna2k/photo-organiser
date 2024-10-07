package dnt.photoorganiser.findduplicates;

import dnt.photoorganiser.findduplicates.archiver.Archiver;
import dnt.photoorganiser.findduplicates.choosers.AlphabeticalPathChooser;
import dnt.photoorganiser.findduplicates.filesizehashcollector.FileSizeHashCollector;
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
import java.util.Set;

import static dnt.photoorganiser.testing.DirectoryAssertions.*;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

public class FindDuplicatesTest extends TestBase
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Path pit;
    private Path album;
    private Path archive;
    private FindDuplicates findDuplicates;
    private TestArchiver archiver;

    @Before
    public void setUp() throws IOException
    {
        album = temporaryFolder.newFolder("Album").toPath();
        pit = temporaryFolder.newFolder("Pit").toPath();
        archive = temporaryFolder.newFolder("Archive").toPath();

        archiver = new TestArchiver();
        findDuplicates = new FindDuplicates(album, pit, new AlphabeticalPathChooser(), archiver, false, true, emptySet());
    }

    @Test
    public void shouldFindDuplicatesInPit() throws IOException
    {
        Files.writeString(new File(pit.toFile(), "file1").toPath(), "unique");
        Files.writeString(new File(pit.toFile(), "file2").toPath(), "same");
        Files.writeString(new File(pit.toFile(), "file3").toPath(), "same");

        findDuplicates.findAndArchive();

        assertThat(archiver.archivedFiles.size()).isEqualTo(1);
        assertThat(archiver.archivedFiles.getFirst().toString()).endsWith("file3");
        assertDirectory(temporaryFolder.getRoot().toPath(),
                "Pit/file1",
                "Pit/file2"
        );
    }

    @Test
    public void shouldWarnIfDuplicatesInPrimaryDirectory() throws IOException
    {
        Files.writeString(new File(album.toFile(), "file1").toPath(), "same");
        Files.writeString(new File(album.toFile(), "file2").toPath(), "same");

        findDuplicates.findAndArchive();

        assertDirectory(temporaryFolder.getRoot().toPath(),
                "Album/file1",
                "Album/file2"
        );
    }

    @Test
    public void ifPrimaryFileHasDuplicateInPitAutoArchivePitDuplicates() throws IOException
    {
        Files.writeString(new File(album.toFile(), "file1").toPath(), "same1");
        Files.writeString(new File(pit.toFile(), "file2").toPath(), "same1");
        Files.writeString(new File(pit.toFile(), "file3").toPath(), "same2");
        Files.writeString(new File(pit.toFile(), "file4").toPath(), "same2");

        findDuplicates.findAndArchive();

        assertThat(archiver.archivedFiles.size()).isEqualTo(2);
        assertDirectory(temporaryFolder.getRoot().toPath(),
                "Album/file1",
                "Pit/file3"
        );
    }

    @Test
    public void shouldExtensions() throws IOException
    {
        Files.writeString(new File(pit.toFile(), "file1").toPath(), "unique");
        Files.writeString(new File(pit.toFile(), "file2").toPath(), "same");
        Files.writeString(new File(pit.toFile(), "file3").toPath(), "same");

        FindDuplicates finder = new FindDuplicates(album, pit, new AlphabeticalPathChooser(), archiver, false, false, Set.of("txt"));
        finder.findAndArchive();

        assertThat(archiver.archivedFiles.size()).isEqualTo(0);
        assertDirectory(temporaryFolder.getRoot().toPath(),
                "Pit/file1",
                "Pit/file2",
                "Pit/file3"
        );
    }

    private static class TestArchiver implements Archiver
    {
        private final List<Path> archivedFiles = new ArrayList<>();

        @Override
        public void add(Path filePath)
        {
            this.archivedFiles.add(filePath);
            assert filePath.toFile().delete() : "Failed to delete " + filePath;
        }

        @Override
        public void close()
        {
        }
    }
}