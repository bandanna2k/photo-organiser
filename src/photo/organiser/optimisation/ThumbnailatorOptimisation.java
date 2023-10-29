package photo.organiser.optimisation;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

public class ThumbnailatorOptimisation implements ImageOptimisation
{
    private final File srcImage;
    private final File destImage;
    private final float quality;

    public ThumbnailatorOptimisation(File srcImage, File destImage, float quality)
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
            Thumbnails.of(srcImage)
                    .scale(1)
                    .outputQuality(quality)
                    .toFile(destImage);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
