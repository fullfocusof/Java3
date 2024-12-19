package org.example.Parser;
import org.example.Ekvus.EkvusParser;
import org.example.HTML.HtmlLoader;

import lombok.*;

import org.example.Habr.HabrParser;
import org.example.Kukla.KuklaParser;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

@Getter
@Setter
public class ParserWorker<T>
{
    T dataList;
    private Parser<T> parser;
    private ParserSettings parserSettings;
    HtmlLoader loader;
    boolean isActive, isHabr;
    public ArrayList<OnNewDataHandler> onNewDataList;
    public ArrayList<OnCompletedHandler> onCompletedList;

    public ParserWorker(HabrParser habrParser)
    {
        onNewDataList = new ArrayList<>();
        onCompletedList = new ArrayList<>();
        isHabr = true;
    }

    public ParserWorker(EkvusParser ekvusParser)
    {
        onNewDataList = new ArrayList<>();
        onCompletedList = new ArrayList<>();
    }

    public ParserWorker(KuklaParser kuk)
    {
        onNewDataList = new ArrayList<>();
        onCompletedList = new ArrayList<>();
    }

    public void setParserSettings(ParserSettings ps)
    {
        parserSettings = ps;
        loader = new HtmlLoader(parserSettings);
    }

    public void Start() throws IOException
    {
        isActive = true;
        Worker();
    }

    public void Abort()
    {
        isActive = false;
    }

    private void Worker() throws IOException
    {
        for (int i = parserSettings.getStartPoint(); i <= parserSettings.getEndPoint(); i++)
        {
            if (!isActive)
            {
                onCompletedList.getFirst().OnCompleted(this);
                return;
            }

            Document document;
            if (isHabr)
            {
                document = loader.GetSourceByPageId(i);
                System.out.println("Страница " + i);
            }
            else document = loader.GetAllSource();

            T result = parser.Parse(document);
            if (isHabr) onNewDataList.getFirst().OnNewData(this, result);
            else dataList = result;
        }
        onCompletedList.getFirst().OnCompleted(this);
        isActive = false;
    }

    public interface OnNewDataHandler<T>
    {
        void OnNewData(Object sender, T e);
    }

    public interface OnCompletedHandler
    {
        void OnCompleted(Object sender);
    }
}