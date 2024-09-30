package dnt.photoorganiser.findduplicates;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

class SizeHashFilesCollector implements BiConsumer<SizeHash, List<Path>>
{
    final Map<SizeHash, List<Path>> sizeHashToFiles = new HashMap<>();

    @Override
    public void accept(SizeHash sizeHash, List<Path> paths)
    {
        sizeHashToFiles.put(sizeHash, paths);
    }
}
