package org.example.HTML;

import org.example.Parser.ParserSettings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HtmlLoader
{
    String url;

    public HtmlLoader(ParserSettings settings)
    {
        url = settings.BASE_URL+"/"+ settings.PREFIX;
    }

    public Document GetSourceByPageId(int id) throws IOException
    {
        String currentUrl = url.replace("{CurrentId}", Integer.toString(id));
        return Jsoup.connect(currentUrl).get();
    }

    public Document GetAllSource() throws IOException
    {
        return Jsoup.connect(url).get();
    }
}