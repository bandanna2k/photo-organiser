package dnt.photoorganiser.organiser;

public class OrganiserTestBase extends FileTestBase
{
    protected static Config getConfig()
    {
        return getConfig(Config.FileDateTimeMode.Creation);
    }

    protected static Config getConfig(Config.FileDateTimeMode fileDateTimeMode)
    {
        Config config = new Config();
        config.execute = true;
        config.fileDateTimeMode = fileDateTimeMode;
        return config;
    }
}
