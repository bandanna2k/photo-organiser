package dnt.photoorganiser.findduplicates;

import dnt.photoorganiser.findduplicates.filesizehashcollector.SizeHash;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;

public class FileSizeHashTestCollector implements BiConsumer<SizeHash, List<Path>>
{
    public final Map<SizeHash, List<Path>> sizeHashToFiles = new HashMap<>();
    public final Set<Path> paths = new HashSet<>();

    @Override
    public void accept(SizeHash sizeHash, List<Path> paths)
    {
        this.paths.addAll(paths);
        sizeHashToFiles.put(sizeHash, paths);
    }
}
