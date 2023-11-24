package syntax;

import org.junit.Test;
import photo.organiser.Main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileTest
{

    public static final int BUFFER = 1024;
    public static final String TEST_STRING = "The quick brown fox jumps over the lazy dog.";

    @Test
    public void testWritingAZipFileToDisk() throws IOException
    {
        String time = new SimpleDateFormat("HH_mm").format(new java.util.Date());
        Map<String, InputStream> filenameToInputStream = Map.of(
                time + "/photo.jpg", Main.class.getResourceAsStream("/photo_medium.jpg"),
                time + "/test.txt", new ByteArrayInputStream(TEST_STRING.getBytes())
        );

        File tempFile = File.createTempFile(ZipFileTest.class.getSimpleName(), ".zip");

        try(final FileOutputStream fos = new FileOutputStream(tempFile);
            final BufferedOutputStream bos = new BufferedOutputStream(fos);
            final ZipOutputStream zos = new ZipOutputStream(bos))
        {
            for (Map.Entry<String, InputStream> entry : filenameToInputStream.entrySet())
            {
                String filename = entry.getKey();
                InputStream inputStream = entry.getValue();

                final ZipEntry zipEntry = new ZipEntry(filename);
                zos.putNextEntry(zipEntry);

                byte data[] = new byte[BUFFER];
                int count;
                while ((count = inputStream.read(data, 0, BUFFER)) != -1)
                {
                    zos.write(data, 0, count);
                }
            }
        }

        System.out.println(tempFile);
    }
}
