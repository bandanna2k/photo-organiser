package photo.organiser.optimisation;

import photo.organiser.imagery.ImageDelta;
import photo.organiser.ui.PhotoFrame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        new Main().start();
    }

    private void start() throws InterruptedException
    {
        List<Thread> threads = new ArrayList<>();
        {
            Thread thread = new Thread(compareImages(0.05f));
            thread.start();
            threads.add(thread);
        }
        {
            Thread thread = new Thread(compareImages(0.80f));
            thread.start();
            threads.add(thread);
        }
        {
            Thread thread = new Thread(compareImages(0.60f));
            thread.start();
            threads.add(thread);
        }

        for (Thread t : threads)
        {
            t.join();
        }
    }

    private static Runnable compareImages(float quality)
    {
        return () ->
        {
            try
            {
                File tempFile = createTempFile();
                File srcImage = new File("/tmp/images/1.jpg");
                ImageOptimisation optimisation = new ImageIoOptimisation(srcImage, tempFile, quality);
                optimisation.optimise();

                ImageDelta imageDelta = new ImageDelta(ImageIO.read(srcImage), ImageIO.read(tempFile));

                PhotoFrame photoFrame = new PhotoFrame(optimisation.getClass().getSimpleName() + " " + quality, srcImage, tempFile);
                System.out.println("Displaying");
                System.out.println("Creating delta");
                BufferedImage delta = imageDelta.delta();
                System.out.println("Finished delta");
                photoFrame.addImage(delta);
                System.out.println("Redisplaying");
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        };
    }

    private static File createTempFile() throws IOException
    {
        File tempFile = Files.createTempFile(ImageIoOptimisation.class.getSimpleName(), ".jpg").toFile();
        tempFile.deleteOnExit();
        return tempFile;
    }
}
