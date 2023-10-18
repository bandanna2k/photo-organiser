package photo.organiser;

import photo.organiser.common.Result;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main
{
    private InputStream inputStream;

    public static void main(String[] args)
    {
        Config config = new Config(args);
        new Main(System.in).start(config);
    }

    public Main(InputStream in)
    {
        inputStream = in;
    }

    void start(final Config config)
    {
        int menuItem;
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        out(config);

        Result<Void, String> validate = config.validate();
        if(validate.isFailure())
        {
            out("Error: " + validate.failure());
            return;
        }

        do
        {
            out("--------------------");
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
        out("Finished");
    }

    private void onMenuItem(int menuItem)
    {
        out("Item choosen: " + menuItem);
    }

    private void out(final Object message)
    {
        System.out.println(message);
    }
}