package photo.organiser.imagery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PanningPanel extends JPanel
{
    public static final int DELTA = 10;

    private int x, y;
    private final int width;
    private final int height;
    BufferedImage img;
    private final static RenderingHints textRenderHints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    private final static RenderingHints imageRenderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final static RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    static int startX, startY;

    public PanningPanel(BufferedImage img)
    {
        x = 0;
        y = 0;
        this.img = img;
        this.width = img.getWidth();
        this.height = img.getHeight();
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent me)
            {
                super.mousePressed(me);
                startX = me.getX();
                startY = me.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent me)
            {
                super.mouseDragged(me);
                onMouseEvent(me);
            }
        });
    }

    public void onMouseEvent(MouseEvent me)
    {
        if (me.getX() < startX)
        {//moving image to right
            x -= DELTA;
        }
        else if (me.getX() > startX)
        {//moving image to left
            x += DELTA;
        }

        if (me.getY() < startY)
        {//moving image up
            y -= DELTA;
        }
        else if (me.getY() > startY)
        {//moving image to down
            y += DELTA;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs)
    {
        super.paintComponent(grphcs);
        Graphics2D g2d = (Graphics2D) grphcs;

        //turn on some nice effects
        applyRenderHints(g2d);

        g2d.drawImage(img, x, y, null);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(width, height);
    }

    public static void applyRenderHints(Graphics2D g2d)
    {
        g2d.setRenderingHints(textRenderHints);
        g2d.setRenderingHints(imageRenderHints);
        g2d.setRenderingHints(renderHints);
    }
}
