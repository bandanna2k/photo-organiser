package photo.organiser.optimisation;

import net.coobird.thumbnailator.Thumbnails;
import utils.ImageUtils;

import java.io.File;
import java.io.IOException;

public class JPEGOptimizerOptimisation implements ImageOptimisation
{
    private final File srcImage;
    private final File destImage;
    private final float quality;

    public JPEGOptimizerOptimisation(File srcImage, File destImage, float quality)
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
            ImageUtils.createJPEG(srcImage, destImage, (int)(quality * 100.0f));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
