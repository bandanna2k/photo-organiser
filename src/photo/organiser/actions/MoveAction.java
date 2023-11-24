package photo.organiser.actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MoveAction extends Action
{
    private final File source;
    private final Path destinationDirectory;

    public MoveAction(File source, Path destinationDirectory)
    {

        this.source = source;
        this.destinationDirectory = destinationDirectory;
    }

    @Override
    public void act()
    {
        try
        {
            Path destination = Path.of(destinationDirectory.toFile().getAbsolutePath(), source.getName());
            Files.move(source.toPath(), destination, StandardCopyOption.ATOMIC_MOVE);
        }
        catch (IOException e)
        {
            errors.add(new ActionError("Failed to move file.", e.getMessage()));
        }
    }

    @Override
    public String toString()
    {
        return "MoveAction{" +
                "source=" + source +
                ", destination=" + destinationDirectory +
                "} " + super.toString();
    }
}
