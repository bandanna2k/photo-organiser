package photo.organiser.actions;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.fail;

public class CopyActionTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private Path folder1;
    private Path folder2;

    @Before
    public void setUp() throws Exception
    {
        folder1 = temporaryFolder.newFolder().toPath();
        folder2 = temporaryFolder.newFolder().toPath();
    }

    @Test
    public void shouldCopyFile() throws IOException
    {
        System.out.println(folder1.toFile().getAbsolutePath());
        System.out.println(folder2.toFile().getAbsolutePath());

        File source = new File(folder1.toFile().getAbsolutePath(), "myFile");
        Files.writeString(source.toPath(), "The quick brown fox jumps over the lazy dog.");
        System.out.println(source.getAbsolutePath() + " " + source.length());

        File destination = new File(folder2.toFile().getAbsolutePath(), "myFileCopy");

        Assertions.assertThat(source.exists()).isTrue();
        Assertions.assertThat(destination.exists()).isFalse();

        CopyAction action = new CopyAction(source, destination);
        action.act();

        Assertions.assertThat(action.errors).describedAs(action.errors.toString()).isEmpty();
        Assertions.assertThat(source.exists()).isTrue();
        Assertions.assertThat(destination.exists()).isTrue();

        fail("Should copy attributes");
    }
}