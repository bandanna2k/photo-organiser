package photo.organiser.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PhotoFrame extends JFrame
{
    private final Map<File, BufferedImage> images = new TreeMap<>(Comparator.reverseOrder());
    private JPanel mainPanel;

    public PhotoFrame(String title, File... imageFiles) throws HeadlessException
    {
        super("Photo Frame - " + title);
        Arrays.stream(imageFiles).forEach(file ->
        {
            try
            {
                images.put(file, ImageIO.read(file));
            }
            catch (IOException e)
            {
                System.out.println(file.getName() + " " + e.getMessage());
                ;
            }
        });
        initComponents();
    }

    public static void main(String[] args)
    {
        PhotoFrame photoFrame = new PhotoFrame("Test", new File("/tmp/images/1.jpg"), new File("/tmp/images/2.jpg"));
    }

    private void initComponents()
    {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        mainPanel = createMainPanel();
        add(mainPanel);
        setVisible(true);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setMaximumSize(dimension);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));

        JButton button = new JButton("OK");
        button.addActionListener(actionEvent ->
        {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        JPanel centrePanel = new JPanel();
        centrePanel.setLayout(new GridLayout(1, images.size(), 5, 5));
        images.entrySet().forEach(imageEntry -> addImageComponent(centrePanel, imageEntry));
        Arrays.stream(centrePanel.getComponents()).forEach(component1 ->
        {
            ImageComponent imageComponent1 = ((ImageComponent) component1);
            Arrays.stream(centrePanel.getComponents()).forEach(component2 ->
            {
                ((ImageComponent) component2).addListener(imageComponent1);
            });
        });
        mainPanel.add(centrePanel, BorderLayout.CENTER);
        mainPanel.add(button, BorderLayout.PAGE_END);
        return mainPanel;
    }

    private static void addImageComponent(Container container, Map.Entry<File, BufferedImage> imageEntry)
    {
        BufferedImage image = imageEntry.getValue();
        if (null == image)
        {
            JLabel label = new JLabel(imageEntry.getKey().getName() + " Failed to load image.");
            container.add(label);
        }
        else
        {
            container.add(new ImageComponent(image, imageEntry.getKey().length()));
        }
    }

    public void addImage(final BufferedImage image)
    {
        images.put(new File(""), image);

        getContentPane().remove(mainPanel);

        mainPanel = createMainPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        revalidate();
    }
}
