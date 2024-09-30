package dnt.photoorganiser.findduplicates.hashing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MD5Hasher implements Hasher
{
    private final MessageDigest md5;

    public MD5Hasher()
    {
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String hash(Path filePath)
    {
        try
        {
            byte[] bytes = Files.readAllBytes(filePath);
            byte[] hash = md5.digest(bytes);
            return Base64.getMimeEncoder().encodeToString(hash);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
