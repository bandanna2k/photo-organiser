package photo.organiser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private PrintWriter user;
    private Main main;
    private Thread appThread;
    private File photos;
    private File duplicates;
    private File awaiting;

    @Before
    public void setUp() throws Exception
    {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        OutputStreamWriter osw = new OutputStreamWriter(pos);
        user = new PrintWriter(osw, true);
        main = new Main(pis);

        File tempDir = temporaryFolder.newFolder();
        photos = new File(tempDir, "Photos");
        photos.mkdir();
        duplicates = new File(tempDir, "Duplicates");
        duplicates.mkdir();
        awaiting = new File(tempDir, "Awaiting");
        awaiting.mkdir();

        addImages();

        appThread = new Thread(() -> main.start(new Config(new String[]{tempDir.getAbsolutePath()})));
        appThread.start();
    }

    @After
    public void tearDown() throws Exception
    {
        appThread.join();
    }

    @Test
    public void shouldExit()
    {
        user.println("99");
        user.println("0");
    }

    @Test
    public void shouldChoseNoFileToProcess()
    {
        user.println("2");
        user.println("0");
        user.println("0");
    }

    @Test
    public void shouldChoseFileToProcess()
    {
        user.println("2");  // Process
        user.println("1");  // Choose hash
        user.println("1");  // Choose destination
        user.println("0");
    }

    private void addImages()
    {
        //addImage("/photo_medium.jpg", "1.jpg");
        addImage("/photo_large.jpg", "2.jpg");
    }

    private void addImage(String name, String image)
    {
        try
        {
            URL resource = this.getClass().getResource(name);
            assert resource != null : "Null resource";
            Files.copy(Path.of(resource.toURI()), new File(awaiting, image).toPath());
        }
        catch (URISyntaxException | IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
