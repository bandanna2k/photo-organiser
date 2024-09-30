package dnt.photoorganiser.findduplicates.choosers;

import java.nio.file.Path;
import java.util.List;

public interface Chooser
{
    int choose(List<Path> paths);
}
