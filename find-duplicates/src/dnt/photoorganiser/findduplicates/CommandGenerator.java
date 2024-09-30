package dnt.photoorganiser.findduplicates;

import dnt.photoorganiser.commands.ArchiveCommands;
import dnt.photoorganiser.commands.BatchOfCommands;
import dnt.photoorganiser.commands.MoveCommand;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CommandGenerator
{
    private final FileSizeHashCollector findDuplicates;
    private final Chooser chooser;

    public CommandGenerator(FileSizeHashCollector findDuplicates, Chooser chooser)
    {
        this.findDuplicates = findDuplicates;
        this.chooser = chooser;
    }

    public void generateCommands() throws IOException
    {
        findDuplicates.walkSource();
    }

    public void forEach()
    {
        findDuplicates.forEachSizeHash((sizeHash, filePaths) -> {
            List<Path> tempFiles = new ArrayList<>(filePaths);
            int choiceIndex = chooser.choose(filePaths);
            Path choice = tempFiles.remove(choiceIndex);

            BatchOfCommands batch = new BatchOfCommands();
            batch.add(new MoveCommand(choice, null));
            batch.add(new ArchiveCommands.Tar(choice, null));
        });
    }

    public void forEach(Object o)
    {

    }
}
