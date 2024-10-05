package dnt.photoorganiser.findduplicates.archiver;

import java.io.Closeable;
import java.nio.file.Path;

public interface Archiver extends Closeable
{
    void add(Path filePath);
}
