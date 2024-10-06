package dnt.photoorganiser.organiser;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class Config
{
    @Parameter(names = "--confirm-files-unique")
    public boolean confirmFilesAreUnique = true;

    @Parameter(names = {"-b", "--before", "--prefix"})
    public List<String> prefixes = new ArrayList<>();

    @Parameter(names = {"-a", "--after", "--postfix"})
    public List<String> postfixes = new ArrayList<>();
}
