package dnt.photoorganiser.findduplicates.archiver;

import java.nio.file.Path;

public interface Archiver
{
    void add(Path filePath);
}
