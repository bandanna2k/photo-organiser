package photo.organiser.optimisation;

import photo.organiser.ui.PhotoFrame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        new Main().start();
    }

    private void start() throws IOException, InterruptedException
    {

        Thread thread1 = new Thread(() ->
        {
            try
            {
                File tempFile = createTempFile();
                File srcImage = new File("/tmp/images/1.jpg");
                new ImageIoOptimisation(srcImage, tempFile, 0.3f).optimise();

                new PhotoFrame(srcImage, tempFile);
                System.out.println("Finished 1");
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() ->
        {
            try
            {
                File tempFile = createTempFile();
                File srcImage = new File("/tmp/images/1.jpg");
                new ThumbnailatorOptimisation(srcImage, tempFile, 0.3f).optimise();

                new PhotoFrame(srcImage, tempFile);
                System.out.println("Finished 2");
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });
        thread2.start();

        thread1.join();
        thread2.join();
    }

    private static File createTempFile() throws IOException
    {
        File tempFile = Files.createTempFile(ImageIoOptimisation.class.getSimpleName(), ".jpg").toFile();
        tempFile.deleteOnExit();
        return tempFile;
    }
}
