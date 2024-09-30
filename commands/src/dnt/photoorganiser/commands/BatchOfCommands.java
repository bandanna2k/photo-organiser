package dnt.photoorganiser.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BatchOfCommands
{
    private final List<Command> commands = new ArrayList<>();

    public void add(Command command)
    {
        commands.add(command);
    }

    public void forEach(Consumer<Command> consumer)
    {
        commands.forEach(consumer);
    }
}
