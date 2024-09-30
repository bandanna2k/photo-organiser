package dnt.photoorganiser.findduplicates.choosers;

import java.nio.file.Path;
import java.util.List;

public class AlphabeticalPathChooser implements Chooser
{
    @Override
    public int choose(List<Path> paths)
    {
        int result = -1;
        Path longestPath = null;
        for (int i = 0; i < paths.size(); i++)
        {
            Path filePath = paths.get(i);
            if(longestPath == null || filePath.toString().compareTo(longestPath.toString()) < 0)
            {
                longestPath = filePath;
                result = i;
            }
        }
        return result;
    }
}
