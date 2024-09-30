package dnt.photoorganiser.findduplicates;

import java.nio.file.Path;
import java.util.List;

public class LongestPathChooser implements Chooser
{
    @Override
    public int choose(List<Path> paths)
    {
        int result = -1;
        Path longestPath = Path.of("/");
        for (int i = 0; i < paths.size(); i++)
        {
            Path filePath = paths.get(i);
            if(filePath.toString().length() > longestPath.toString().length())
            {
                longestPath = filePath;
                result = i;
            }
        }
        return result;
    }
}
