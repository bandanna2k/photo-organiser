package photo.organiser.ui;

import photo.organiser.common.HumanReadableBytes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class ImageComponent extends JPanel
{
    private final BufferedImage image;
    private final long sizeBytes;

    public ImageComponent(final File image) throws IOException
    {
        this(ImageIO.read(image), image.length());

        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
    }

    public ImageComponent(final BufferedImage image, final long sizeBytes)
    {
        this.image = image;
        this.sizeBytes = sizeBytes;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String sizeText = HumanReadableBytes.bytesIntoHumanReadable(sizeBytes);

        Font font = new Font("Arial", Font.BOLD, 20);
        g2.setFont(font);
        g2.drawString(sizeText, 10, 20);
    }
}
