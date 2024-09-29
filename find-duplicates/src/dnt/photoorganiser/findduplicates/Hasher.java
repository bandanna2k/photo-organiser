package dnt.photoorganiser.findduplicates;

import java.nio.file.Path;

public interface Hasher
{
    String hash(Path filePath);
}
