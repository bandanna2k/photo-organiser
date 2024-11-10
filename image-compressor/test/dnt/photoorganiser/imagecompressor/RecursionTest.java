package dnt.photoorganiser.imagecompressor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class RecursionTest
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
        temporaryFolder.newFile("file2.jpeg");
        File subFolder = temporaryFolder.newFolder("subFolder");
        new File(subFolder, "subFolder_file1").createNewFile();
        new File(subFolder, "subFolder_file2.jpg").createNewFile();
        new File(subFolder, "subFolder_file3.jpeg").createNewFile();
    }

    @Test
    public void testRecursion()
    {
        {
            main = new Main(new Config().extensions(List.of("jpg")));
            assertThat(main.start()).isEqualTo(2);
        }
        {
            main = new Main(new Config().extensions(List.of("jpg", "jpeg")));
            assertThat(main.start()).isEqualTo(4);
        }
    }
}
