package dnt.photoorganiser.organiser;

import com.beust.jcommander.JCommander;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

public class OrganiserTest extends FileTestBase
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException
    {
        temporaryFolder.newFolder("main1/sub1/sub1a");
        writeFile(new File(temporaryFolder.getRoot() + "/main1/sub1/file1a"), CONTENT, MORNING, NOON, NIGHT);
    }

    @Test
    public void shouldWalkDirectory() throws IOException
    {
        try (PipedInputStream pis = new PipedInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(pis));
        )
        {
            try(PrintStream writer = new PrintStream(new PipedOutputStream(pis)))
            {
                FileInfoWalker walker = new FileInfoWalker(temporaryFolder.getRoot().toPath(), writer);
                walker.walkDirectory();
            }

            String output;
            while(null != (output = reader.readLine()))
            {
                System.out.println(output);
            }
        }
    }
}