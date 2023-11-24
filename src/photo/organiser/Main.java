package photo.organiser;

import photo.organiser.actions.Action;
import photo.organiser.actions.CopyAction;
import photo.organiser.actions.MoveAction;
import photo.organiser.common.Result;
import photo.organiser.optimisation.ImageIoOptimisation;
import photo.organiser.optimisation.ImageOptimisation;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main
{
    private HashFinder hashFinder;
    private Map<String, Record> hashToRecord = new ConcurrentHashMap<>();
    private final Config config;
    private BufferedReader reader;

    public static void main(String[] args)
    {
        Config config = new Config(args);
        new Main(config, System.in).start();
    }

    public Main(Config config, InputStream in)
    {
        this.config = config;
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    public void start()
    {
        int menuItem;

        out(config);

        Result<Void, String> validate = config.validate();
        if (validate.isFailure())
        {
            out("Error: " + validate.failure());
            return;
        }

        hashFinder = new HashFinder(config.triageDirectory, hashToRecord);

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
        } while (menuItem != 0);

        searchingThread.interrupt();
        out("Finished");
    }

    private void onMenuItem(int menuItem)
    {
        out("Item chosen: " + menuItem);
        switch (menuItem)
        {
            case 1:
                out(hashFinder.getStatus());
                break;
            case 2:
                Optional<Map.Entry<String, Record>> any = hashToRecord.entrySet().stream().findFirst();
                any.ifPresentOrElse(entry ->
                {
                    List<Action> actions = getActions(entry.getKey(), entry.getValue());
                    out(actions);
                    hashToRecord.remove(entry.getKey());
                }, () -> out("No file to process."));
                break;
        }
    }

    private List<Action> getActions(final String hash, final Record record)
    {
        List<Action> actions = new ArrayList<>();
        SystemIO io = new SystemIO();
        actions.addAll(choseFileToKeep(record, io));
        actions.addAll(getActionsToOptimiseImage(record));

            /*
                A/A.optimised.jpg*
                A/A.jpg

                Move temp file to chosen folder
                Move all old files to archive
             */
            /*

        actions.addAll(getActionsToMoveImages(record));

            /*
                A/A.jpg*
                B/A.jpg
                C/A.jpg
                D/A.jpg

                Keep/A/A*.jpg
                Old/B/A.jpg
                Old/C/A.jpg
                Old/D/A.jpg
                 */

        return null;
    }

    private Collection<Action> choseFileToKeep(Record record, IO io)
    {
        out("Choose a file:");
        for (int i = 0; i < record.files.size() ; i++)
        {
            File file = record.files.get(i);
            out((i+1) + ": " + file.getAbsolutePath());
        }
        int choice = record.files.size() == 1 ? 1 :
                Integer.parseInt(io.ask("Which file?"));
        File chosenFile = record.files.get(choice - 1);
        out("File chosen: " + chosenFile);
        record.chosenFile = chosenFile;
        return Collections.emptyList();
    }

    private Collection<Action> getActionsToMoveImages(Record record)
    {

        return Collections.emptyList();
    }

    private Collection<Action> getActionsToOptimiseImage(Record record)
    {
        try
        {
            List<Action> actions = new ArrayList<>();

            File srcImage = record.chosenFile;
            File tempFile = Files.createTempFile(ImageIoOptimisation.class.getSimpleName(), ".jpg").toFile();

            ImageOptimisation optimisation = new ImageIoOptimisation(srcImage, tempFile, 0.8f);
            optimisation.optimise();

            if((srcImage.length() * 0.5) > tempFile.length())
            {
                actions.add(new CopyAction(tempFile, new File(createOptimisedFilename(tempFile.getAbsolutePath()))));
                record.files.forEach(file -> {
                    actions.add(new MoveAction(srcImage, config.archiveDirectory));
                });
                record.shouldOptimise = true;
            }
            else
            {
                tempFile.deleteOnExit();
            }
            return actions;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    static String createOptimisedFilename(String absolutePath)
    {
        int separatorIndex = absolutePath.lastIndexOf(".");
        String start = absolutePath.substring(0, separatorIndex);
        String end = absolutePath.substring(separatorIndex);
        return start + ".optimised" + end;
    }

    private void out(final Object message)
    {
        System.out.println(message);
    }

    private class SystemIO implements IO
    {
        @Override
        public String ask(String message)
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
        }
        @Override
        public void out(Object object)
        {
            Main.this.out(object);
        }
    }
}