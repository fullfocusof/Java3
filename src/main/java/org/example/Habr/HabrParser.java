package org.example.Habr;

import org.example.Parser.Parser;
import org.example.Model.Article;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HabrParser implements Parser<ArrayList<Article>>
{
    @Override
    public ArrayList<Article> Parse(Document document)
    {
        ArrayList<Article> list = new ArrayList<>();

        Elements arts = document.body().select("article.tm-articles-list__item");
        for (Element art : arts)
        {
            Article temp = new Article();
            temp.setArtName(art.select("h2[data-test-id=\"articleTitle\"] span").text());
            temp.setAuthor(art.select("a.tm-user-info__username").text());

            Elements tagsElems = art.select("div.tm-publication-hubs a.tm-publication-hub__link span");
            StringBuilder sbTags = new StringBuilder();
            for (Element tag : tagsElems)
            {
                if (tag.attributesSize() == 0) sbTags.append(tag.text()).append(",");
            }
            temp.setTags(sbTags.toString().split(","));

            String viewsStr = art.select("span.tm-icon-counter__value").text().trim();
            int views, mp;
            if (viewsStr.endsWith("K"))
            {
                mp = 1000;
                viewsStr = viewsStr.substring(0, viewsStr.length() - 1);
                double value = Double.parseDouble(viewsStr);
                views = (int) (value * mp);
            }
            else views = Integer.parseInt(viewsStr);
            temp.setViews(views);

            String readingTimeStr = art.select("span.tm-article-reading-time__label").text();
            int readingTime = Integer.parseInt(readingTimeStr.substring(0, readingTimeStr.indexOf(" ")));
            temp.setReadingTime(readingTime); // Duration.ofMinutes(readingTime)

            Elements URLElem = art.select("div.tm-article-body.tm-article-snippet__lead>div.tm-article-snippet__cover_cover.tm-article-snippet__cover");
            if (!URLElem.isEmpty())
            {
                try
                {
                    URL picURL = new URL(art.select("img.tm-article-snippet__lead-image").attr("src"));
                    String pucURLStr = picURL.toString();
                    temp.setPicURL(picURL);

                    downloadImage(picURL, pucURLStr.substring(pucURLStr.lastIndexOf("/")).trim());
                }
                catch (MalformedURLException e)
                {
                    System.out.println(e.getMessage());
                }
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd, hh:mm");
            Date timeOfPub = new Date();
            try
            {
                String timeOfPubStr = art.select("a.tm-article-datetime-published.tm-article-datetime-published_link time").attr("title");
                timeOfPub = formatter.parse(timeOfPubStr);
            }
            catch (ParseException e)
            {
                System.out.println(e.getMessage());
            }
            temp.setTimeOfPublication(timeOfPub);

            Elements previewElems = art.select("div.article-formatted-body.article-formatted-body.article-formatted-body_version-2 p");
            StringBuilder sbPreview = new StringBuilder();
            for (Element elem : previewElems)
            {
                sbPreview.append(elem.text()).append("\n");
            }
            temp.setPreview(sbPreview.toString());

            list.add(temp);
        }

        return list;
    }

    private static void downloadImage(URL imgURL, String filename)
    {
        byte[] buffer = new byte[2048];
        int n;
        String URLStr = imgURL.toString();
        String extension = URLStr.substring(URLStr.lastIndexOf("."));

        try (InputStream in = imgURL.openStream())
        {
            OutputStream os = new FileOutputStream( "src\\main\\resources\\images" + "\\" + filename + extension);
            while ((n = in.read(buffer)) != -1 )
            {
                os.write(buffer, 0, n);
            }
            os.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}