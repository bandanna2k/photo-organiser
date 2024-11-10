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

public class MainExecuteTest
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
        config.execute = true;

        main = new Main(config);
    }

    @Test
    public void shouldChangeFileTimes() throws IOException
    {
        assertThat(main.start()).isEqualTo(2);
    }

//    private void displayFiles() throws IOException
//    {
//        Files.walkFileTree(temporaryFolder.getRoot().toPath(), new SimpleFileVisitor<>()
//        {
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
//            {
//                FileTime creationTime = getCreationTime(file.toFile());
//                FileTime modifiedTime = getModifiedTime(file.toFile());
//                FileTime lastAccessedTime = getLastAccessedTime(file.toFile());
//
//                System.out.printf("[%s], CT %s, MT %s, LAT %s%n", file.toFile().getAbsolutePath(), creationTime, modifiedTime, lastAccessedTime);
//                return super.visitFile(file, attrs);
//            }
//        });
//    }
}