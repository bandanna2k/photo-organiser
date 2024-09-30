package dnt.photoorganiser.findduplicates;

import dnt.photoorganiser.commands.BatchOfCommands;
import dnt.photoorganiser.commands.Command;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class CommandCollector implements Consumer<BatchOfCommands>
{
    final List<Command> commands = new ArrayList<>();

    @Override
    public void accept(BatchOfCommands batchOfCommands)
    {
        commands.forEach(commands::add);
    }
}
