package dnt.photoorganiser.findduplicates;

import java.nio.file.Path;
import java.util.List;

public interface Chooser
{
    int choose(List<Path> paths);
}
