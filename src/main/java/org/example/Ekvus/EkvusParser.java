package org.example.Ekvus;

import org.example.Model.Performance;
import org.example.Parser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EkvusParser implements Parser<ArrayList<Performance>>
{
    @Override
    public ArrayList<Performance> Parse(Document document)
    {
        ArrayList<Performance> list = new ArrayList<>();

        int year = -1;

        Elements perfs = document.body().select("table.myafisha tbody tr");
        for (Element perf : perfs)
        {
            String yearStr = perf.select("td h2").text();
            if (!yearStr.isEmpty())
            {
                year = Integer.parseInt(yearStr.substring(yearStr.indexOf(" ") + 1));
                continue;
            }

            Performance temp = new Performance();

            String perfNameAndAge = perf.select("td a:has(span)").text();
            int sizeToTrim = perf.select("td a span").text().length();
            temp.setPerfName(perfNameAndAge.substring(0, perfNameAndAge.length() - sizeToTrim));

            temp.setAgeRate(Integer.parseInt(perf.select("td a span").text().substring(0,1)));

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, hh:mm");
            Date timeOfPerf = new Date();
            try
            {
                String timeOfPerfStr = perf.select("font[color=\"#CCCCCC\"]").text().replace(" в ", ", ");
                timeOfPerf = formatter.parse(timeOfPerfStr);
                timeOfPerf.setYear(year - 1900);
            }
            catch (ParseException e)
            {
                System.out.println(e.getMessage());
            }
            temp.setTimeOfPerf(timeOfPerf);

            try
            {
                Document perfPage = Jsoup.connect(EkvusSettings.BASE_URL + perf.select("td>a:has(span)").attr("href")).get();

                URL picURL = new URL(EkvusSettings.BASE_URL + perfPage.select("p>a[href^=\"/media/show/\"]").attr("href"));
                temp.setPicURL(picURL);

                Elements els = perfPage.select("div[style='text-align:right; font-size: 15px;']");
                for (Element el : els)
                {
                    if (el.text().startsWith("Продолжительность"))
                    {
                        String durStr = el.text();
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(durStr);

                        StringBuilder formattedDur = new StringBuilder();
                        while (matcher.find())
                        {
                            formattedDur.append(matcher.group()).append(" ");
                        }
                        String hours = formattedDur.substring(0, formattedDur.indexOf(" ")).trim();
                        formattedDur.delete(0, formattedDur.indexOf(" ") + 1);
                        String minutes = !formattedDur.isEmpty() ? formattedDur.toString().trim() : "0";

                        Duration perfDur = Duration.ofHours(Integer.parseInt(hours)).plusMinutes(Integer.parseInt(minutes));
                        temp.setPerfDuration(perfDur);
                    }
                }
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }

            list.add(temp);
        }

        return list;
    }
}
