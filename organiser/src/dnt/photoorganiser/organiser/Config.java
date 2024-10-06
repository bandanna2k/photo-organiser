package dnt.photoorganiser.organiser;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class Config
{
    public enum FileDateTimeMode
    {
        Creation,
        Modified,
        LastAccessed,
    }

    @Parameter(names = "--confirm-files-unique")
    public boolean confirmFilesAreUnique = true;

    @Parameter(names = {"-b", "--before", "--prefix"})
    public List<String> prefixes = new ArrayList<>();

    @Parameter(names = {"-a", "--after", "--postfix"})
    public List<String> postfixes = new ArrayList<>();

    @Parameter(names = {"-e", "--execute"})
    public boolean execute = false;

    @Parameter(names = {"--mode"})
    public FileDateTimeMode fileDateTimeMode = FileDateTimeMode.Modified;

    public boolean isPreviewMode()
    {
        return !execute;
    }
}
