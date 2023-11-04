package photo.organiser;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public abstract class Constants
{
    public static final ZoneOffset ZONE_OFFSET = OffsetDateTime.now().getOffset();
}
