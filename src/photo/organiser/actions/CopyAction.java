package photo.organiser.actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CopyAction extends Action
{
    public final File sourceFile;
    public final File destinationFile;

    public CopyAction(File sourceFile, File destinationFile)
    {
        this.sourceFile = sourceFile;
        this.destinationFile = destinationFile;
    }

    @Override
    public void act()
    {
        try
        {
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
        }
        catch (IOException e)
        {
            errors.add(new ActionError("Failed to move file.", e.getMessage()));
        }
    }

    @Override
    public String toString()
    {
        return "CopyAction{" +
                "srcFile=" + sourceFile +
                ", dstFile=" + destinationFile +
                '}';
    }
}
