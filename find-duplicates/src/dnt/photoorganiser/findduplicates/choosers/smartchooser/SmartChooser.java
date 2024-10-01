package dnt.photoorganiser.findduplicates.choosers.smartchooser;

import dnt.photoorganiser.findduplicates.choosers.Chooser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static dnt.photoorganiser.findduplicates.choosers.smartchooser.Score.score;

public class SmartChooser implements Chooser
{
    private final BufferedReader reader;
    private final List<Path> selectedPaths = new ArrayList<>();

    public SmartChooser(BufferedReader reader)
    {
        this.reader = reader;
    }

    @Override
    public int choose(List<Path> paths)
    {
        int recommendationIndex = recommendation(paths);

        System.out.println("------------------");
        for (int i = 0; i < paths.size(); i++)
        {
            if(i == recommendationIndex)
            {
                System.out.printf("(%d)\t%s\tRECOMMENDATION%n", i, paths.get(i));
            }
            else
            {
                System.out.printf("(%d)\t%s%n", i, paths.get(i));
            }
        }

        System.out.println("------------------");
        System.out.println("Choose a file.");
        int choice = -1;
        while(choice < 0 || choice >= paths.size())
        {
            try
            {
                choice = Integer.parseInt(reader.readLine());
            }
            catch (IOException e)
            {
                System.err.println("ERROR: Invalid choice.");
            }
        }
        selectedPaths.add(paths.get(choice));
        return choice;
    }

    public int recommendation(List<Path> currentFilePaths)
    {
        int maxScore = -1;
        int recommendation = -1;

        for (Path alreadyChosenFile : selectedPaths)
        {
            for (int i = 0; i < currentFilePaths.size(); i++)
            {
                Path currentFilePath = currentFilePaths.get(i);
                int score = score(alreadyChosenFile, currentFilePath);
                if (score > maxScore)
                {
                    maxScore = score;
                    recommendation = i;
                }
            }
        }
        return recommendation;
    }
}
