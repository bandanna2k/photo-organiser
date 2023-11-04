package photo.organiser;

import photo.organiser.common.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main
{
    private HashFinder hashFinder;
    private Map<String, Record> hashToRecord = new HashMap<>();
    private BufferedReader reader;

    public static void main(String[] args)
    {
        Config config = new Config(args);
        new Main(System.in).start(config);
    }

    public Main(InputStream in)
    {
        reader = new BufferedReader(new InputStreamReader(in));
    }

    public void start(final Config config)
    {
        int menuItem;

        out(config);

        Result<Void, String> validate = config.validate();
        if(validate.isFailure())
        {
            out("Error: " + validate.failure());
            return;
        }

        hashFinder = new HashFinder(config.dir, hashToRecord);

        Thread searchingThread = new Thread(() -> hashFinder.start());
        searchingThread.start();
        try
        {
            searchingThread.join();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        do
        {
            out("--------------------");
            out("(1)  Status");
            out("(2)  Process");
            out("(0)  Exit");
            out("--------------------");
            try
            {
                String input = reader.readLine();
                menuItem = Integer.parseInt(input);
                onMenuItem(menuItem);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        while(menuItem != 0);

        searchingThread.interrupt();
        out("Finished");
    }

    private void onMenuItem(int menuItem)
    {
        out("Item choosen: " + menuItem);
        switch (menuItem)
        {
            case 1:
                out(hashFinder.getStatus());
                break;
            case 2:
                Optional<Map.Entry<String, Record>> any = hashToRecord.entrySet().stream().findAny();
                any.ifPresentOrElse(entry -> process(entry), () -> out("No file to process."));
                out("Next action.");
                break;
        }
    }

    private void process(Map.Entry<String, Record> entry)
    {
        FileProcessor fileProcessor = new FileProcessor(
                message ->
                {
                    try
                    {
                        out(message);
                        return reader.readLine();
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                },
                entry.getValue());
        fileProcessor.start();
    }

    private void out(final Object message)
    {
        System.out.println(message);
    }
}