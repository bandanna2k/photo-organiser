import com.beust.jcommander.Parameter;

public class Config
{
    @Parameter(names = "--confirm-files-unique")
    public boolean confirmFilesAreUnique = true;
}
