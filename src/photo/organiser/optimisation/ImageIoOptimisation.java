package photo.organiser.optimisation;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ImageIoOptimisation implements ImageOptimisation
{
    private final File srcImage;
    private final File destImage;
    private final float quality;

    public ImageIoOptimisation(File srcImage, File destImage, float quality)
    {
        this.srcImage = srcImage;
        this.destImage = destImage;
        this.quality = quality;
    }

    @Override
    public void optimise()
    {
        try
        {
            BufferedImage inputImage = ImageIO.read(srcImage);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();

            ImageOutputStream outputStream = ImageIO.createImageOutputStream(destImage);
            writer.setOutput(outputStream);

            ImageWriteParam params = writer.getDefaultWriteParam();
            params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            params.setCompressionQuality(quality);

            writer.write(null, new IIOImage(inputImage, null, null), params);

            outputStream.close();
            writer.dispose();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
