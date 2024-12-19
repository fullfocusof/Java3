package org.example.Model;

import lombok.*;

import java.net.URL;
import java.time.Duration;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Performance
{
    String perfName;
    Date timeOfPerf;
    Duration perfDuration;
    int ageRate;
    URL picURL;

    @Override
    public String toString()
    {
        String duration = "";
        if (perfDuration != null) duration += perfDuration.toHours() + " ч " + perfDuration.toMinutesPart() + " мин";
        else duration = "неизвестно";
        return timeOfPerf + "\n" +
                perfName + "\n" +
                "Продолжительность: " + duration + "\n" +
                "Возрастное ограничение: " + ageRate + "+\n" +
                "PicURL: " + picURL + "\n";
    }
}
