package dnt.photoorganiser.imagecompressor;

import dnt.photoorganiser.commands.ArchiveCommands;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainPreviewTest
{
    private Main main;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException
    {
        System.setProperty("user.dir", temporaryFolder.getRoot().toString());

        Config config = new Config().quality(0.8f);
        main = new Main(config);
    }

    @Test
    public void name() throws URISyntaxException
    {
        Path file = Paths.get(Main.class.getResource("/photos.tar.gz").toURI());
        new ArchiveCommands.Untar(temporaryFolder.getRoot().toPath(), file).execute();

        main.start();
    }
}
