package photo.organiser;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class FileSystemMain
{
    public static void main(String[] args) throws IOException
    {
        new FileSystemMain().start();
    }

    private void start() throws IOException
    {
        Files.walk(Path.of("."), 1, FileVisitOption.FOLLOW_LINKS)
                .filter(path -> !".".equals(path.toString()) && !"..".equals(path.toString()))
                .findAny()
                .stream().forEach(p ->
                {
                    String type = getFileType(p.toFile());
                    String bfa = getBasicFileAttributes(p.toFile());
                    System.out.println(p + " " + type + " " + bfa);

                    try
                    {
                        FileTime creationTime = (FileTime) Files.getAttribute(p, "creationTime");
                        System.out.println(creationTime);

                        FileTime modifiedTime = (FileTime) Files.getAttribute(p, "lastModifiedTime");
                        System.out.println(modifiedTime);

                        FileTime accessedTime = (FileTime) Files.getAttribute(p, "lastAccessTime");
                        System.out.println(accessedTime);
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }

                });
    }

    private static String getBasicFileAttributes(File file)
    {
        try
        {
            BasicFileAttributes bfa = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return "Created:" + bfa.creationTime() + ", Modified:" + bfa.lastModifiedTime() + ", Accessed:" + bfa.lastAccessTime();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static String getFileType(File file)
    {
        try
        {
            return Files.getFileStore(file.toPath()).type();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
