package dnt.filetimeutil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import static dnt.photoorganiser.filetime.FileTimeOperations.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class MainDontExecuteTest
{
    protected static final Date DATE_2000_JAN = toDate(LocalDateTime.of(2000, 1, 1, 12, 55, 00));
    protected static final Date DATE_2000_FEB = toDate(LocalDateTime.of(2000, 2, 2, 12, 56, 00));
    protected static final Date DATE_2000_MAR = toDate(LocalDateTime.of(2000, 3, 3, 12, 57, 00));

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Main main;

    @Before
    public void setUp() throws IOException
    {
        File dir2000Jan = temporaryFolder.newFolder("2000", "January");
        File file1 = new File(dir2000Jan, "file1");
        File file2 = new File(dir2000Jan, "file2");
        File file3 = new File(dir2000Jan, "file3");
        assertTrue(file1.createNewFile());
        assertTrue(file2.createNewFile());
        assertTrue(file3.createNewFile());
        setFileTimes(file1, toFileTime(DATE_2000_JAN), toFileTime(new Date()), toFileTime(DATE_2000_JAN));
        setFileTimes(file2, toFileTime(DATE_2000_FEB), toFileTime(new Date()), toFileTime(DATE_2000_FEB));
        setFileTimes(file3, toFileTime(DATE_2000_MAR), toFileTime(new Date()), toFileTime(DATE_2000_MAR));

        Config config = new Config();
        config.path = temporaryFolder.getRoot().getAbsolutePath();

        main = new Main(config);
    }

    @Test
    public void shouldNotChangeFileTimes() throws IOException
    {
        assertThat(main.start()).isEqualTo(0);
    }
}