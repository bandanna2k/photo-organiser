package dnt.photoorganiser.findduplicates.filesizehashcollector;

import dnt.photoorganiser.findduplicates.FileSizeHashTestCollector;

import java.io.IOException;

public abstract class FileSizeHashCollectorTestBase
{
    protected FileSizeHashCollector fileSizeHashCollector;
    protected final FileSizeHashTestCollector collector = new FileSizeHashTestCollector();

    protected void findDuplicatesAndCollect() throws IOException
    {
        findDuplicatesAndCollect(fileSizeHashCollector, collector);
    }

    protected void findDuplicatesAndCollect(
            FileSizeHashCollector fshCollector,
            FileSizeHashTestCollector collector) throws IOException
    {
        fshCollector.walkSource();
        fshCollector.forEachSizeHash(collector);
    }
}
