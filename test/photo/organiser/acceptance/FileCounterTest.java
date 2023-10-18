package photo.organiser.acceptance;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FileCounterTest extends TestBase
{

    @Test
    public void shouldCountFiles() throws IOException
    {
        File newFile = new File(photosDir.toFile(), "new");
        newFile.createNewFile();

        user.println(MENU_ITEM_STATUS);
        sleep(TimeUnit.SECONDS.toMillis(1));
        user.println(MENU_ITEM_STATUS);
        user.println(MENU_ITEM_EXIT);
    }

}
