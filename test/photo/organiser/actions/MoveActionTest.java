package photo.organiser.actions;

import jdk.jshell.execution.Util;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import photo.organiser.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.fail;
import static photo.organiser.Utilities.*;

public class MoveActionTest
{
    public static final LocalDateTime TIME_1 = LocalDateTime.of(2023, 11, 23, 18, 32, 59);
    public static final LocalDateTime TIME_2 = LocalDateTime.of(2023, 11, 23, 18, 32, 59);
    public static final LocalDateTime TIME_3 = LocalDateTime.of(2023, 11, 23, 18, 32, 59);

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
    public void shouldMoveFile() throws IOException
    {
        System.out.println(folder1.toFile().getAbsolutePath());
        System.out.println(folder2.toFile().getAbsolutePath());

        File source = new File(folder1.toFile().getAbsolutePath(), "myFile");
        Files.writeString(source.toPath(), "The quick brown fox jumps over the lazy dog.");
        System.out.println(source.getAbsolutePath() + " " + source.length());
        setFileTimes(source, TIME_1, TIME_2, TIME_3);

        File destination = new File(folder2.toFile().getAbsolutePath(), "myFile");

        Assertions.assertThat(source.exists()).isTrue();
        Assertions.assertThat(destination.exists()).isFalse();

        MoveAction action = new MoveAction(source, folder2);
        action.act();

        Assertions.assertThat(action.errors).describedAs(action.errors.toString()).isEmpty();
        Assertions.assertThat(source.exists()).isFalse();
        Assertions.assertThat(destination.exists()).isTrue();
        Assertions.assertThat(Utilities.getCreationTime(destination)).isEqualTo(TIME_1);
    }
}