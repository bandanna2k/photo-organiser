package photo.organiser;

import org.assertj.core.api.Assertions;
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

import static photo.organiser.Main.createOptimisedFilename;

public class MainUnitTest
{
    @Test
    public void testOptimisedPath()
    {
        Assertions.assertThat(createOptimisedFilename("/home/user/image/image1.jpg"))
                .isEqualTo("/home/user/image/image1.optimised.jpg");
    }
}
