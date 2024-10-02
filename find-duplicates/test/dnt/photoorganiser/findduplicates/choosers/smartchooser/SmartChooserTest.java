package dnt.photoorganiser.findduplicates.choosers.smartchooser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

public class SmartChooserTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception
    {
        temporaryFolder.create();
    }
    @After
    public void tearDown()
    {
        temporaryFolder.delete();
    }

    @Test(timeout = 1000L)
    public void testLoadSaveUsingReader() throws IOException
    {
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream(pis);
        PrintWriter writer = new PrintWriter(pos);
        BufferedReader reader = new BufferedReader(new InputStreamReader(pis));

        {
            SmartChooser smartChooser = new SmartChooser(reader);

            writer.println("1");
            writer.println("1");
            writer.flush();
            smartChooser.choose(List.of(
                    Path.of("hello", "partner", "my", "old", "friend"),
                    Path.of("ha", "ha", "ha")
            ));
            smartChooser.choose(List.of(
                    Path.of("hello", "is", "it", "me", "you're", "looking", "for"),
                    Path.of("he", "he", "he")
            ));

            smartChooser.save();
        }
        {
            SmartChooser smartChooser = new SmartChooser(reader);
            smartChooser.load();
            assertThat(smartChooser.selectedPaths.size()).isEqualTo(2);
        }
    }

    @Test
    public void testLoadSave()
    {
        {
            SmartChooser smartChooser = new SmartChooser(null);

            smartChooser.selectedPaths.addAll(List.of(
                    Path.of("hello", "partner", "my", "old", "friend"),
                    Path.of("ha", "ha", "ha")
            ));

            smartChooser.save();
        }
        {
            SmartChooser smartChooser = new SmartChooser(null);
            smartChooser.load();
            assertThat(smartChooser.selectedPaths.size()).isEqualTo(2);
        }
    }

    @Test
    public void testPath() throws JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        {
            SmartChooser smartChooser = new SmartChooser(null);
            smartChooser.selectedPaths.add(Path.of("a", "b"));
            smartChooser.selectedPaths.add(Path.of("a", "b"));
            System.out.println(objectMapper.writeValueAsString(smartChooser.selectedPaths));
            assertThat(smartChooser.selectedPaths.size()).isEqualTo(1);
        }
        {
            Set<String> set = new TreeSet<>();
            /*
"file:///home/northd/Code/photo-organiser/photos/./Pit/file1",
"file:///home/northd/Code/photo-organiser/photos/./Pit/file2",
             */
            set.add(Path.of("a", "b").toString());
            set.add(Path.of("a", "b").toString());
            System.out.println(objectMapper.writeValueAsString(set));
            assertThat(set.size()).isEqualTo(1);
        }
    }
}