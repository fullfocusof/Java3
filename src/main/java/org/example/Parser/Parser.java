package org.example.Parser;

import org.jsoup.nodes.Document;

public interface Parser<T>
{
    T Parse(Document doc);
}
