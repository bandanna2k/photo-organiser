package photo.organiser.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class PhotoFrame extends JFrame
{
    private final File imageLeft;
    private final File imageRight;

    public PhotoFrame(final File imageLeft, final File imageRight) throws HeadlessException
    {
        super("Photo Frame");
        this.imageLeft = imageLeft;
        this.imageRight = imageRight;
        init();
    }

    public static void main(String[] args)
    {
        new PhotoFrame(new File("/tmp/images/1.jpg"), new File("/tmp/images/2.jpg"));
    }

    private void init()
    {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(createMainPanel());
        setVisible(true);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setMaximumSize(dimension);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5,5));

        JButton button = new JButton("OK");
        button.addActionListener(actionEvent -> {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new GridLayout(1, 2, 5, 5));
        addImageComponent(centrePanel, BorderLayout.WEST, imageLeft);
        addImageComponent(centrePanel, BorderLayout.EAST, imageRight);

        mainPanel.add(centrePanel, BorderLayout.CENTER);
        mainPanel.add(button, BorderLayout.PAGE_END);
        return mainPanel;
    }

    private static void addImageComponent(Container container, String orientation, File image)
    {
        try
        {
            container.add(new ImageComponent(image), orientation);
        }
        catch (IOException e)
        {
            JLabel label = new JLabel(e.getMessage());
            label.setHorizontalAlignment(SwingConstants.CENTER);
            container.add(label, orientation);
        }
    }
}
