package dnt.photoorganiser.findduplicates;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;

public class CommandGeneratorTest
{
    private CommandGenerator commandGenerator;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Path source;
    private FileSizeHashCollector fileSizeHashCollector;
    private CommandCollector commandCollector;

    @Before
    public void setUp() throws IOException
    {
//        destination = temporaryFolder.newFolder("Album").toPath();
        source = temporaryFolder.newFolder("Pit").toPath();
        //      archive = temporaryFolder.newFolder("Archive").toPath();

        commandCollector = new CommandCollector();

        fileSizeHashCollector = new FileSizeHashCollector(source);
        commandGenerator = new CommandGenerator(fileSizeHashCollector, new LongestPathChooser());
    }


    @Test
    public void shouldNotMovePrimaryDuplicate() throws IOException
    {
        commandGenerator.generateCommands();
        commandGenerator.forEach(commandCollector);
    }

    /*

    Album >
    Pit >



     */
}