package photo.organiser.acceptance;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import photo.organiser.Config;
import photo.organiser.Main;

import java.io.*;
import java.nio.file.Path;

public abstract class TestBase
{
    protected static final String MENU_ITEM_STATUS = "1";
    protected static final String MENU_ITEM_EXIT = "0";

    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder();

    protected PrintWriter user;
    protected Main main;
    private Thread appThread;
    protected Path photosDir;
    protected Path duplicatesDir;
    protected Path awaiting;

    @Before
    public void setUp() throws Exception
    {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        OutputStreamWriter osw = new OutputStreamWriter(pos);
        user = new PrintWriter(osw, true);
        main = new Main(pis);

        File tempDir = this.tempDir.newFolder();

        photosDir = new File(tempDir, "Photos").toPath();
        photosDir.toFile().mkdir();

        duplicatesDir = new File(tempDir, "Duplicates").toPath();
        duplicatesDir.toFile().mkdir();

        awaiting = new File(tempDir, "Awaiting").toPath();
        awaiting.toFile().mkdir();

        appThread = new Thread(() -> main.start(new Config(new String[]{tempDir.getAbsolutePath()})));
        appThread.start();
    }

    @After
    public void tearDown() throws Exception
    {
        appThread.join();
    }

    protected void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
