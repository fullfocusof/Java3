package org.example.Habr;

import org.example.Parser.ParserSettings;

public class HabrSettings extends ParserSettings
{
    public HabrSettings(int start, int end)
    {
        if (start > end) throw new IllegalArgumentException();
        startPoint = start;
        endPoint = end;
        BASE_URL = "https://habr.com/ru/all";
        PREFIX = "page{CurrentId}";
    }
}