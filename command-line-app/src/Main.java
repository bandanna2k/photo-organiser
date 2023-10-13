import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main
{
    public static void main(String[] args)
    {
        Config config = new Config(args);
        new Main().start(config);
    }

    private void start(Config config)
    {
        int menuItem;
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        do
        {
            out("--------------------");
            out("(0)  Exit");
            out("--------------------");
            try
            {
                menuItem = Integer.parseInt(reader.readLine());
                onMenuItem(menuItem);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        while(menuItem != 0);
    }

    private void onMenuItem(int menuItem)
    {
        out("INFO Choosen item." + menuItem);
    }

    private void out(final String message)
    {
        System.out.println(message);
    }
}