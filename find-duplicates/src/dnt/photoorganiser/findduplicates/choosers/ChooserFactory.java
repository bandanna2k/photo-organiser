package dnt.photoorganiser.findduplicates.choosers;

import dnt.photoorganiser.findduplicates.Config;

import java.io.BufferedReader;

public class ChooserFactory
{
    private final BufferedReader reader;

    public ChooserFactory(BufferedReader reader)
    {
        this.reader = reader;
    }

    public Chooser newInstance(Config.ChooserType chooser)
    {
        return switch (chooser)
        {
            case AutoChooser -> new AlphabeticalPathChooser();
            case CommandLineChooser -> new CommandLineChooser(reader);
        };
    }
}
