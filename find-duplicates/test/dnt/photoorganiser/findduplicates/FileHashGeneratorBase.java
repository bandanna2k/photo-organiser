package dnt.photoorganiser.findduplicates;

import java.io.IOException;

public abstract class FileHashGeneratorBase
{
    protected FileSizeHashCollector fileSizeHashCollector;
    protected final SizeHashFilesCollector collector = new SizeHashFilesCollector();

    protected void findDuplicatesAndCollect() throws IOException
    {
        fileSizeHashCollector.walkSource();
        fileSizeHashCollector.forEachSizeHash(collector);
    }

}
