package dnt.photoorganiser.imagecompressor;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class FiltersTest
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
    }

    @Test
    public void textExtensions()
    {
        {
            main = new Main(new Config().extensions(Collections.singletonList("jpg")));
            assertThat(main.start()).isEqualTo(1);
        }
        {
            main = new Main(new Config().extensions(List.of("jpg", "jpeg")));
            assertThat(main.start()).isEqualTo(2);
        }
    }

    @Test
    public void textContains()
    {
        {
            main = new Main(new Config().extensions(emptyList()).contains("file2"));
            assertThat(main.start()).isEqualTo(1);
        }
        {
            main = new Main(new Config().extensions(emptyList()).contains("file1"));
            assertThat(main.start()).isEqualTo(2);
        }
    }

    @Test
    public void textRegex()
    {
        {
            main = new Main(new Config().extensions(emptyList()).regex(".*ile2.*"));
            assertThat(main.start()).isEqualTo(1);
        }
        {
            main = new Main(new Config().extensions(emptyList()).regex(".*ile1.*"));
            assertThat(main.start()).isEqualTo(2);
        }
    }
}
