package dnt.photoorganiser.findduplicates.choosers.smartchooser;

import java.nio.file.Path;

public abstract class Score
{
    static int score(Path filePath1, Path filePath2)
    {
        return score(filePath1.toString(), filePath2.toString());
    }
    static int score(String file1, String file2)
    {
        int score = 0;
        int letterLoopMax = Math.min(file1.length(), file2.length());

        // Forward score
        for (int i = 0; i < letterLoopMax; i++)
        {
            if (file1.charAt(i) == file2.charAt(i))
            {
                score++;
            }
        }

        // Backward score
        for (int i = 0; i < letterLoopMax; i++)
        {
            if (file1.charAt(file1.length() - i - 1) == file2.charAt(file2.length() - i - 1))
            {
                score++;
            }
        }
        return score;
    }
}
