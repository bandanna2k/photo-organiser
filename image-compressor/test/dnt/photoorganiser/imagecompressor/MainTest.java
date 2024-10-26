package dnt.photoorganiser.imagecompressor;

import dnt.photoorganiser.commands.ArchiveCommands;
import dnt.photoorganiser.commands.CopyCommand;
import dnt.photoorganiser.commands.MoveCommand;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainTest
{
    private Main main;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException
    {
        System.setProperty("user.dir", temporaryFolder.getRoot().toString());

        main = new Main(new String[] {
                "--quality", "0.8",
                "--execute"
        });
    }

    @Test
    public void name() throws URISyntaxException
    {
        Path file = Paths.get(Main.class.getResource("/photos.tar.gz").toURI());
        new ArchiveCommands.Untar(temporaryFolder.getRoot().toPath(), file).execute();

        main.start();
    }
}
