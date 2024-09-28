package dnt.photoorganiser.commands;

import dnt.common.Result;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

public class TouchCommandTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private Path root;

    @Before
    public void setUp() throws Exception
    {
        temporaryFolder.create();
        root = temporaryFolder.getRoot().toPath();
    }

    @Test
    public void testTouch()
    {
        Result<Integer, String> execute = new TouchCommand(root.resolve("touch.txt")).execute();
        assertThat(execute.success()).isEqualTo(0);
    }
}