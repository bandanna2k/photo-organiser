package syntax;

import org.junit.Test;

import java.io.*;

public class PipedIoTest
{
    @Test
    public void testPipedIO() throws IOException, InterruptedException
    {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(pos);
        InputStreamReader isr = new InputStreamReader(pis);
        BufferedReader reader = new BufferedReader(isr);

        OutputStreamWriter osw = new OutputStreamWriter(pos);
        PrintWriter writer = new PrintWriter(osw, true);

        Thread thread = new Thread(() -> writer.println("6"));
        thread.start();
        String b = reader.readLine();
        System.out.println(b);
        thread.join();
    }
}
