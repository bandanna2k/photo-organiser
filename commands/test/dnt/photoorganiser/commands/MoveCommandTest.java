package dnt.photoorganiser.commands;

import dnt.common.Result;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MoveCommandTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception
    {
        temporaryFolder.create();
    }

    @Test
    public void shouldMoveFile() throws IOException
    {
        Path source = temporaryFolder.newFile().toPath();
        Path destination = temporaryFolder.newFolder().toPath();
        System.out.println(source);
        System.out.println(destination);

        Result<Integer, String> moveResult = new MoveCommand(source, destination).execute();
        assertTrue(moveResult.isSuccess());
        File sourceFile = source.toFile();
        assertFalse(sourceFile.exists());
        File movedFile = new File(destination.toFile(), sourceFile.getName());
        assertTrue(movedFile.exists());
    }
}