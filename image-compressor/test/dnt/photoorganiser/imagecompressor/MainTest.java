package dnt.photoorganiser.imagecompressor;

import com.beust.jcommander.JCommander;
import dnt.photoorganiser.filetime.FileTimeOperations;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class MainTest
{
    private Main main;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException
    {
        System.setProperty("user.dir", temporaryFolder.getRoot().toString());

        temporaryFolder.newFile("file1");
        temporaryFolder.newFile("file1.jpg");
        temporaryFolder.newFile("file2.jpg");

        main = new Main(new Config());
    }

    @Test
    public void name()
    {
        main.start();
    }
}
