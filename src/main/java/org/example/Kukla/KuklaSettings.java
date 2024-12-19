package org.example.Kukla;

import org.example.Parser.ParserSettings;

public class KuklaSettings extends ParserSettings
{
    public KuklaSettings(int start, int end)
    {
        if (start > end) throw new IllegalArgumentException();
        startPoint = start;
        endPoint = end;
        BASE_URL = "https://kirovkukla.ru";
        PREFIX = "afisha";
    }
}