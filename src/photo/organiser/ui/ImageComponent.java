package photo.organiser.ui;

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

    public ImageComponent(final File image) throws IOException
    {
        this.image = ImageIO.read(image);

        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}
