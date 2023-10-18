package photo.organiser.files;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class FileSystemTest
{
    @Test
    public void shouldShowFileSystemTypeOfFile() throws IOException
    {
        File temp = org.assertj.core.util.Files.newTemporaryFile();

        String format = Files.getFileStore(temp.toPath()).type();
        System.out.println(format);
    }

    @Test
    public void shouldShowFileSystems() throws IOException
    {
        FileSystem fs = FileSystems.getDefault();
        fs.supportedFileAttributeViews().forEach(fav -> System.out.println("File attribute view:" + fav));
    }

    @Test
    public void shouldShowFileSystemInformation()
    {
        // Creating an object of FileSystem class in the
        // main() method using getDefault() method
        FileSystem filesystem = FileSystems.getDefault();

        // Display message only
        System.out.println("FileSystem created successfully");

        // Checking if file system is open or close
        if (filesystem.isOpen())
        {
            System.out.println("File system is open");
        }
        else
        {
            System.out.println("File system is close");
        }

        // Check if file system is read-only
        if (filesystem.isReadOnly())
        {
            System.out.println("File system is Read-only");
        }
        else
        {
            System.out.println("File system is not Read-only");
        }

        // Now, print the name separator
        // using getSeparator() method
        System.out.println("Separator: " + filesystem.getSeparator());

        // Print hash value of this file system
        // using hashCode() method
        System.out.println("Hashcode: " + filesystem.hashCode());

        // Print provider of this file system
        System.out.println("Provider: " + filesystem.provider().toString());
        System.out.println("Provider scheme: " + filesystem.provider().getScheme());
    }
}
