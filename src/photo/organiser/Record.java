package photo.organiser;

import java.io.File;
import java.util.List;

public class Record
{
    public List<File> files;
    public File chosenFile;
    public boolean shouldOptimise;

    public Record(List<File> files)
    {
        this.files = files;
    }
}
