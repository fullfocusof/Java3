package org.example.IOnNewData;

import org.example.Model.Performance;
import org.example.Parser.ParserWorker;

import java.util.ArrayList;

public class NewDataPerformance implements ParserWorker.OnNewDataHandler<ArrayList<Performance>>
{
    @Override
    public void OnNewData(Object sender, ArrayList<Performance> args)
    {
//        for (Performance perf : args)
//        {
//            System.out.println(perf.toString());
//        }
    }
}