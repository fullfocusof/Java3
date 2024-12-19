package org.example.IOnCompleted;

import org.example.Parser.ParserWorker;

public class Completed implements ParserWorker.OnCompletedHandler {

    @Override
    public void OnCompleted(Object sender)
    {
        System.out.println("Загрузка завершена");
    }
}