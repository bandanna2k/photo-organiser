package dnt.photoorganiser.findduplicates;

import dnt.photoorganiser.findduplicates.filesizehashcollector.SizeHash;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SizeHashFilesCollector implements BiConsumer<SizeHash, List<Path>>
{
    public final Map<SizeHash, List<Path>> sizeHashToFiles = new HashMap<>();

    @Override
    public void accept(SizeHash sizeHash, List<Path> paths)
    {
        sizeHashToFiles.put(sizeHash, paths);
    }
}
