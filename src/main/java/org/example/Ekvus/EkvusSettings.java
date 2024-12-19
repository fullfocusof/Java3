package org.example.Ekvus;

import org.example.Parser.ParserSettings;

public class EkvusSettings extends ParserSettings
{
    public EkvusSettings()
    {
//        if (start > end) throw new IllegalArgumentException();
//        startPoint = start;
//        endPoint = end;
        BASE_URL = "https://ekvus-kirov.ru";
        PREFIX = "afisha";
    }
}
