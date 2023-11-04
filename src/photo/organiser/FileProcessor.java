package photo.organiser;

import photo.organiser.imagery.ImageDelta;
import photo.organiser.optimisation.ImageIoOptimisation;
import photo.organiser.optimisation.ImageOptimisation;
import photo.organiser.ui.PhotoFrame;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

import static photo.organiser.Constants.ZONE_OFFSET;

public class FileProcessor
{
    private final IO io;
    private final Record record;
    private final Set<String> chosenFiles = new TreeSet<>();

    public FileProcessor(IO io, Record record)
    {
        this.io = io;
        this.record = record;
    }

    public void start()
    {
//        getCompressActions();
        getChosenFileActions();
    }

    private void getChosenFileActions()
    {
        io.out("Choose a file.");
        io.out("0) <Skip>.");

        int i = 0;
        for (File file : record.files)
        {
            io.out(++i + ") " + file.getAbsolutePath());
        }
        try
        {
            int choice = Integer.parseInt(io.ask("Choose a file."));
            if(0 == choice)
            {
                io.out("Skipping, no file chosen.");
            }
            else
            {
                File file = record.files.get(choice - 1);
                chosenFiles.add(file.getAbsolutePath());
                io.out("Chosen file: " + file);

                moveFiles(file);
            }
        }
        catch(Exception ex)
        {
            io.out("Unable to choose file. " + ex.getMessage());
        }
    }

    private void moveFiles(File file) throws IOException
    {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        LocalDateTime creationTime = LocalDateTime.ofInstant(basicFileAttributes.creationTime().toInstant(), ZONE_OFFSET);
        LocalDateTime modifiedTime = LocalDateTime.ofInstant(basicFileAttributes.lastModifiedTime().toInstant(), ZONE_OFFSET);
        LocalDateTime accessedTime = LocalDateTime.ofInstant(basicFileAttributes.lastAccessTime().toInstant(), ZONE_OFFSET);

        try
        {
            io.out(String.format("1) by creation date: %d > %s", creationTime.getYear(), month(creationTime.getMonthValue())));
            io.out(String.format("2) by modified date: %d > %s", modifiedTime.getYear(), month(modifiedTime.getMonthValue())));
            io.out(String.format("3) by accessed date: %d > %s", accessedTime.getYear(), month(accessedTime.getMonthValue())));

            io.out(String.format("4) by creation date: %d > %s > %02d", creationTime.getYear(), month(creationTime.getMonthValue()), creationTime.getDayOfMonth()));
            io.out(String.format("5) by modified date: %d > %s > %02d", modifiedTime.getYear(), month(modifiedTime.getMonthValue()), modifiedTime.getDayOfMonth()));
            io.out(String.format("6) by accessed date: %d > %s > %02d", accessedTime.getYear(), month(accessedTime.getMonthValue()), accessedTime.getDayOfMonth()));

            int choice = Integer.parseInt(io.ask("Where would you like to move the chosen file to? Chosen file."));
            if(0 == choice)
            {
                io.out("No destination chosen. Skipping");
            }
            else
            {
                io.out("Destination chosen: " + choice);
            }
        }
        catch(Exception ex)
        {
            io.out("Unable to chose file. " + ex.getMessage());
        }
    }

    private void getCompressActions()
    {
        try
        {
            File src = record.files.get(0);
            File dest = File.createTempFile("optimisation", ".jpg");

            ImageOptimisation optimisation = new ImageIoOptimisation(src, dest, 0.8f);
            optimisation.optimise();

            ImageDelta imageDelta = new ImageDelta(ImageIO.read(src), ImageIO.read(dest));

            double ratioSrcDest = ((double)dest.length()) / ((double)src.length());

            System.out.println(src + " " + src.length());
            System.out.println(dest + " " + dest.length());
            System.out.println("Ratio: " + ratioSrcDest);

            if(ratioSrcDest < 0.5)
            {
                System.out.println("Candidate to compress.");

                PhotoFrame frame = new PhotoFrame("Comparison", src, dest);
                frame.addImage(imageDelta.delta());
                frame.setVisible(true);

                boolean doCompress = Boolean.parseBoolean(io.ask("Do you want to compress?"));
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static String month(int month)
    {
        return MONTH_NAMES[month];
    }
}
