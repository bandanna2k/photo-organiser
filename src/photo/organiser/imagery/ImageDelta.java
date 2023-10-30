package photo.organiser.imagery;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class ImageDelta
{
    private static Font FONT = new Font("Arial", Font.BOLD, 20);

    private final BufferedImage image1;
    private final BufferedImage image2;

    public ImageDelta(final BufferedImage image1, final BufferedImage image2)
    {
        this.image1 = image1;
        this.image2 = image2;
    }

    public BufferedImage delta()
    {
        // convert images to pixel arrays...
        final int w = image1.getWidth();
        final int h = image1.getHeight();

        final int[] p1 = image1.getRGB(0, 0, w, h, null, 0, w);
        final int[] p2 = image2.getRGB(0, 0, w, h, null, 0, w);
        final int length = p1.length;

        final double[] delta = new double[length];
        double maxDelta = Double.MIN_VALUE;

        // compare img1 to img2, pixel by pixel. If different, highlight img1's pixel...
        for (int i = 0; i < length; i++)
        {
            delta[i] = abs(p1[i] - p2[i]);
            maxDelta = max(delta[i], maxDelta);
        }

        double multiplier = 255.0 / maxDelta;
        for (int i = 0; i < length; i++)
        {
            p1[i] = (int)(delta[i] * multiplier);
        }

        // save img1's pixels to a new BufferedImage, and return it...
        // (May require TYPE_INT_ARGB)
        final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        result.setRGB(0, 0, w, h, p1, 0, w);

        String text = String.format("0 to %.2f -> 0 to 255", maxDelta);

        Graphics2D g2d = result.createGraphics();
        g2d.setPaint(Color.WHITE);
        g2d.setFont(FONT);
        g2d.drawString(text, 10, 40);

        return result;
    }

    public BufferedImage delta2()
    {
        byte[] magenta = {-1, 0, -1};
        byte[] image1Bytes = ((DataBufferByte)image1.getRaster().getDataBuffer()).getData();
        byte[] image2Bytes = ((DataBufferByte)image2.getRaster().getDataBuffer()).getData();
        byte[] imageDelta = new byte[image1Bytes.length];

        for (int i = 1; i < image1Bytes.length; i += 4)
        {
            if (image1Bytes[i] != image2Bytes[i])
            {
                System.arraycopy(magenta, 0, image1Bytes, i, 3);
            }
        }
        return null;
    }
}
