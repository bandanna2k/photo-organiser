package dnt.photoorganiser.findduplicates;

import com.beust.jcommander.ParameterException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static dnt.photoorganiser.testing.DirectoryAssertions.assertDirectoryUsingRegex;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConfigTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Path root;

    @Before
    public void setUp() throws Exception
    {
        root = temporaryFolder.getRoot().toPath();
        temporaryFolder.newFolder("Album");
        temporaryFolder.newFolder("Pit");
        temporaryFolder.newFolder("Archive");
    }

    @Test
    public void shouldGetConfig()
    {
        String[] args = {
                root + "/Album",
                root + "/Pit",
                root + "/Archive",
        };
        Config config = Main.getConfig(args);
        assertThat(config.getPrimaryDirectory()).isNotNull();
        assertThat(config.getSecondaryDirectory()).isNotNull();
        assertThat(config.getArchiveDirectory()).isNotNull();
    }

    @Test
    public void shouldFailWithoutAllDirectories()
    {
        String[] args = {
                root + "/Album",
                root + "/Pit",
        };
        assertThatThrownBy(() -> {
            Main.getConfig(args);
        }).isInstanceOf(ParameterException.class);
    }

    @Test
    public void shouldFailWithNonExistingDirectory() throws IOException
    {
        //temporaryFolder.newFolder("Nowhere").toPath();
        String[] args = {
                root + "/Album",
                root + "/Pit",
                root + "/Nowhere",
        };
        assertThatThrownBy(() -> {
            Main.getConfig(args);
        }).isInstanceOf(ParameterException.class);
    }

    @Test
    public void shouldFailWithTooManyDirectories() throws IOException
    {
        temporaryFolder.newFolder("XXX").toPath();
        String[] args = {
                root + "/Album",
                root + "/Pit",
                root + "/Archive",
                root + "/XXX",
        };
        assertThatThrownBy(() -> {
            Main.getConfig(args);
        }).isInstanceOf(ParameterException.class);
    }
}
