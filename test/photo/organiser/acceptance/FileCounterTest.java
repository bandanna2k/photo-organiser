package photo.organiser.acceptance;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class FileCounterTest extends TestBase
{

    @Test
    public void shouldCountFiles() throws IOException
    {
        File newFile = new File(photosDir.toFile(), "new");
        Files.writeString(newFile.toPath(), "unique");

        File newFile2 = new File(photosDir.toFile(), "new2");
        Files.writeString(newFile2.toPath(), "same");

        File newFile3 = new File(photosDir.toFile(), "new3");
        Files.writeString(newFile3.toPath(), "same");

        user.println(MENU_ITEM_STATUS);
        sleep(TimeUnit.SECONDS.toMillis(1));
        user.println(MENU_ITEM_STATUS);
        user.println(MENU_ITEM_EXIT);
    }

}
