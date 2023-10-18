package photo.organiser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

public class MainTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private PrintWriter user;
    private Main main;
    private Thread appThread;

    @Before
    public void setUp() throws Exception
    {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        OutputStreamWriter osw = new OutputStreamWriter(pos);
        user = new PrintWriter(osw, true);
        main = new Main(pis);

        File tempDir = temporaryFolder.newFolder();
        new File(tempDir, "Photos").mkdir();
        new File(tempDir, "Duplicates").mkdir();
        new File(tempDir, "Awaiting").mkdir();

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
}
