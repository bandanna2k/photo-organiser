package dnt.photoorganiser.findduplicates.hashing;

import java.nio.file.Path;

public interface Hasher
{
    String hash(Path filePath);
}
