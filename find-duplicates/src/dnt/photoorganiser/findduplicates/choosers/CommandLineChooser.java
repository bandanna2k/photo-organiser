package dnt.photoorganiser.findduplicates.choosers;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CommandLineChooser implements Chooser
{
    private final BufferedReader reader;

    public CommandLineChooser(BufferedReader reader)
    {
        this.reader = reader;
    }

    @Override
    public int choose(List<Path> paths)
    {
        try
        {
            System.out.println("------------------");
            for (int i = 0; i < paths.size(); i++)
            {
                System.out.printf("(%d)\t%s%n", i, paths.get(i));
            }

            System.out.println("------------------");
            System.out.println("Choose a file.");
            return Integer.parseInt(reader.readLine());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
