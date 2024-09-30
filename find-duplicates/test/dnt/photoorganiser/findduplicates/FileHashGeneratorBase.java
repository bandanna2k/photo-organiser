package dnt.photoorganiser.findduplicates;

import java.io.IOException;

public abstract class FileHashGeneratorBase
{
    protected CollectSizeHashFiles collectSizeHashFiles;
    protected final SizeHashFilesCollector collector = new SizeHashFilesCollector();

    protected void findDuplicatesAndCollect() throws IOException
    {
        collectSizeHashFiles.walkSource();
        collectSizeHashFiles.forEachSizeHash(collector);
    }

}
