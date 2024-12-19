package org.example.IOnNewData;

import java.util.ArrayList;

import org.example.Parser.ParserWorker;
import org.example.Model.Article;

public class NewDataArticle implements ParserWorker.OnNewDataHandler<ArrayList<Article>>
{
    @Override
    public void OnNewData(Object sender, ArrayList<Article> args)
    {
        for (Article art : args)
        {
            System.out.println(art.toString());
        }

        Article.toGsonFile(args, "outputGson.json");
    }
}