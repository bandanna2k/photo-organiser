package dnt.photoorganiser.findduplicates.filesizehashcollector;

import dnt.photoorganiser.findduplicates.SizeHashFilesCollector;

import java.io.IOException;

public abstract class FileSizeHashCollectorTestBase
{
    protected FileSizeHashCollector fileSizeHashCollector;
    protected final SizeHashFilesCollector collector = new SizeHashFilesCollector();

    protected void findDuplicatesAndCollect() throws IOException
    {
        fileSizeHashCollector.walkSource();
        fileSizeHashCollector.forEachSizeHash(collector);
    }
}
