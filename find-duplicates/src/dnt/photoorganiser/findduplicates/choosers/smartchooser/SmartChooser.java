package dnt.photoorganiser.findduplicates.choosers.smartchooser;

import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.photoorganiser.findduplicates.choosers.Chooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static dnt.photoorganiser.findduplicates.choosers.smartchooser.Score.score;

public class SmartChooser implements Chooser
{
    private final BufferedReader reader;
    private final ObjectMapper objectMapper;

    Set<Path> selectedPaths = new TreeSet<>();


    public SmartChooser(BufferedReader reader)
    {
        this.reader = reader;
        objectMapper = new ObjectMapper();
        Runtime.getRuntime().addShutdownHook(new Thread(this::save));
    }

    public void load()
    {
        File file = new File("SmartChooser.json");
        if(!file.exists()) return;

        try
        {
            Path[] paths = objectMapper.readValue(file, Path[].class);
            selectedPaths = new TreeSet<>(Arrays.stream(paths).toList());
            System.out.println("INFO: Selected paths loaded.");
        }
        catch (Exception e)
        {
            System.err.println("ERROR: Failed to load. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    void save()
    {
        try
        {
            File file = new File("SmartChooser.json");
            Set<String> pathStrings = selectedPaths.stream().map(Path::toString).collect(Collectors.toSet());
            objectMapper.writeValue(file, pathStrings);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int choose(List<Path> paths)
    {
        int recommendationIndex = recommendation(paths);

        System.out.println("------------------");
        for (int i = 0; i < paths.size(); i++)
        {
            Path filePath = paths.get(i);
            System.out.printf("(%d)\t%s\t%s", i, filePath.toFile().getName(), filePath);
            System.out.println(i == recommendationIndex ? "\tRECOMMENDATION" : "");
        }

        System.out.println("------------------");
        System.out.printf("Choose a file to keep. 0 to %d, or 'r' for RECOMMENDATION. All others will be archived.%n", paths.size() - 1);
        int choice = -1;
        while(choice < 0 || choice >= paths.size())
        {
            try
            {
                String input = reader.readLine();
                if("r".equalsIgnoreCase(input))
                {
                    choice = recommendationIndex;
                }
                else
                {
                    choice = Integer.parseInt(input);
                }
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
