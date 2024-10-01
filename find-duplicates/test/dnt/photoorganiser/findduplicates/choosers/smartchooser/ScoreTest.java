package dnt.photoorganiser.findduplicates.choosers.smartchooser;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import static dnt.photoorganiser.findduplicates.choosers.smartchooser.Score.score;

public class ScoreTest
{
    @Test
    public void testScore()
    {
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(score("12345/12345", "123456/12345")).isEqualTo(11);
        softly.assertThat(score("12345", "123456")).isEqualTo(5);

        softly.assertThat(score(
                        "/mnt/sdc1/Old/bandanna2k/zoe.north.2013/2013/Scans/2012 Aug 1st/OBSTETRIC ULTRASOUND 0024.jpg",
                        "/mnt/sdc1/Old/bandanna2k/zoe.north.2013/2013/Scans/2012 Aug 1st/OBSTETRIC ULTRASOUND 0024.jpg"))
                .isEqualTo(186);

        softly.assertAll();
    }
}