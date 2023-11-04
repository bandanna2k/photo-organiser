package photo.organiser;

import photo.organiser.imagery.ImageDelta;
import photo.organiser.optimisation.ImageIoOptimisation;
import photo.organiser.optimisation.ImageOptimisation;
import photo.organiser.ui.PhotoFrame;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class FileProcessor
{
    private final IO io;
    private final Record record;

    public FileProcessor(IO io, Record record)
    {
        this.io = io;
        this.record = record;
    }

    public void start()
    {
        try
        {
            File src = record.files.get(0);
            File dest = File.createTempFile("optimisation", ".jpg");

            ImageOptimisation optimisation = new ImageIoOptimisation(src, dest, 0.8f);
            optimisation.optimise();

            ImageDelta imageDelta = new ImageDelta(ImageIO.read(src), ImageIO.read(dest));

            double ratioSrcDest = ((double)dest.length()) / ((double)src.length());

            System.out.println(src + " " + src.length());
            System.out.println(dest + " " + dest.length());
            System.out.println("Ratio: " + ratioSrcDest);

            if(ratioSrcDest < 0.5)
            {
                System.out.println("Candidate to compress.");

                PhotoFrame frame = new PhotoFrame("Comparison", src, dest);
                frame.addImage(imageDelta.delta());
                frame.setVisible(true);

                boolean doCompress = Boolean.parseBoolean(io.ask("Do you want to compress?"));
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
