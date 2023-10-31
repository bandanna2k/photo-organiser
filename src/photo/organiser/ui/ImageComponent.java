package photo.organiser.ui;

import photo.organiser.common.HumanReadableBytes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageComponent extends PanningPanel implements ImageComponentListener
{
    private final List<ImageComponentListener> listeners = new ArrayList<>();
    private final long sizeBytes;

    public ImageComponent(final File image) throws IOException
    {
        this(ImageIO.read(image), image.length());
    }

    public ImageComponent(final BufferedImage image, final long sizeBytes)
    {
        super(image);
        this.sizeBytes = sizeBytes;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String sizeText = HumanReadableBytes.bytesIntoHumanReadable(sizeBytes);

        Font font = new Font("Arial", Font.BOLD, 20);
        g2.setFont(font);
        g2.drawString(sizeText, 10, 20);
    }

    @Override
    public void onMouseEvent(MouseEvent me)
    {
        super.onMouseEvent(me);
        synchronized (listeners)
        {
            listeners.forEach(listener -> listener.onMouseEvent(this, me));
        }
    }

    public void onMouseEvent(ImageComponent imageComponent, MouseEvent me)
    {
        if(imageComponent == this)
        {
            return;
        }
        super.onMouseEvent(me);
    }

    public void addListener(ImageComponentListener listener)
    {
        synchronized (listeners)
        {
            listeners.add(listener);
        }
    }
}
