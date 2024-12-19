package org.example;

import org.example.Ekvus.EkvusParser;
import org.example.Ekvus.EkvusSettings;
import org.example.Habr.HabrParser;
import org.example.Habr.HabrSettings;
import org.example.IOnCompleted.Completed;
import org.example.IOnNewData.NewDataArticle;
import org.example.IOnNewData.NewDataPerformance;
import org.example.Kukla.KuklaParser;
import org.example.Kukla.KuklaSettings;
import org.example.Model.Article;
import org.example.Model.Performance;
import org.example.Parser.ParserWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{
    static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args)
    {

        ParserWorker<ArrayList<Article>> parserArts = null;
        ParserWorker<ArrayList<Performance>> parserPerfs = null;

        Scanner sc = new Scanner(System.in);
        String userInput;
        System.out.print(">");

        boolean exitProg = false;
        while (!exitProg)
        {
            userInput = sc.nextLine();
            switch (userInput)
            {
                case "exit":
                {
                    exitProg = true;
                }
                break;

                case "help":
                {
                    String sb = String.format("%-25s %s%n", "habr", "Загрузить данные с habr.ru") +
                            String.format("%-25s %s%n", "ekvus", "Загрузить данные с ekvus.ru") +
                            String.format("%-25s %s%n", "afisha", "Агрегатор спектаклей") +
                            String.format("%-25s %s%n", "exit", "Выйти из программы");
                    System.out.println(sb);
                }
                break;

                case "habr":
                {
                    System.out.print("Введите начальную и конечную страницы через пробел: ");
                    String[] userPages = sc.nextLine().split(" ");
                    System.out.println(" ");
                    if (userPages.length == 2)
                    {
                        int startPage = Integer.parseInt(userPages[0]);
                        int endPage = Integer.parseInt(userPages[1]);

                        if (startPage > endPage)
                        {
                            System.out.println("Неверный ввод");
                            break;
                        }

                        HabrParser hb = new HabrParser();
                        parserArts = new ParserWorker<>(hb);
                        parserArts.setParser(hb);
                        parserArts.setParserSettings(new HabrSettings(startPage, endPage));

                        parserArts.onNewDataList.add(new NewDataArticle());
                        parserArts.onCompletedList.add(new Completed());

                        try
                        {
                            parserArts.Start();
                            Thread.sleep(10000);
                            parserArts.Abort();
                        }
                        catch (IOException | InterruptedException e)
                        {
                            log.log(Level.SEVERE, "Не удалось получить доступ к web-странице");
                        }
                    }
                    else
                    {
                        System.out.println("Неверный ввод");
                        break;
                    }
                }
                break;

                case "ekvus":
                {
                    EkvusParser ekv = new EkvusParser();
                    parserPerfs = new ParserWorker<>(ekv);
                    parserPerfs.setParser(ekv);
                    parserPerfs.setParserSettings(new EkvusSettings());

                    parserPerfs.onNewDataList.add(new NewDataPerformance());
                    parserPerfs.onCompletedList.add(new Completed());

                    try
                    {
                        parserPerfs.Start();
                        Thread.sleep(10000);
                        parserPerfs.Abort();
                    }
                    catch (IOException | InterruptedException e)
                    {
                        log.log(Level.SEVERE, "Не удалось получить доступ к web-странице");
                    }
                }
                break;

                case "afisha":
                {
                    if (parserPerfs == null)
                    {
                        System.out.println("Данные о спектаклях отсутствуют");
                        break;
                    }

                    ArrayList<Performance> perfs = parserPerfs.getDataList();
                    ArrayList<Performance> outPerfs = new ArrayList<>();

                    Date curDate = new Date(), endDate = null;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(curDate);

                    System.out.println("Выберите период: ");
                    String sb = String.format("%-50s %s%n", "day", "Спектакли в ближайшие сутки") +
                            String.format("%-50s %s%n", "week", "Спектакли в ближайшую неделю") +
                            String.format("%-50s %s%n", "month", "Спектакли в ближайший месяц") +
                            String.format("%-50s %s%n", "year", "Спектакли в ближайший год");
                    System.out.println(sb);

                    System.out.print(">");
                    String userChoice = sc.nextLine();
                    switch (userChoice)
                    {
                        case "day":
                        {
                            calendar.add(Calendar.DAY_OF_MONTH, 1);
                            endDate = calendar.getTime();
                        }
                        break;

                        case "week":
                        {
                            calendar.add(Calendar.WEEK_OF_YEAR, 1);
                            endDate = calendar.getTime();
                        }
                        break;

                        case "month":
                        {
                            calendar.add(Calendar.MONTH, 1);
                            endDate = calendar.getTime();
                        }
                        break;

                        case "year":
                        {
                            calendar.add(Calendar.YEAR, 1);
                            endDate = calendar.getTime();
                        }
                        break;
                    }

                    for (int i = 0; i < perfs.size(); i++)
                    {
                        if (perfs.get(i).getTimeOfPerf().compareTo(endDate) < 0) outPerfs.add(perfs.get(i));
                    }

                    if (outPerfs.isEmpty()) System.out.println("Спектакли за выбранный период отсутствуют");
                    else
                    {
                        for (int i = 0; i < outPerfs.size(); i++)
                        {
                            System.out.println(outPerfs.get(i).toString());
                        }
                    }
                }
                break;

//                case "kukla":
//                {
//                    KuklaParser kuk = new KuklaParser();
//                    parserPerfs = new ParserWorker<>(kuk);
//                    parserPerfs.setParser(kuk);
//                    parserPerfs.setParserSettings(new KuklaSettings(1,1)); // ??????? старт конец
//                }
//                break;

//                case "dramteatr":
//                {
//
//                }
//                break;

                default:
                {
                    System.out.println("\"" + userInput + "\"" + " не является командой");
                }
                break;
            }

            if (!exitProg)
            {
                System.out.print(">");
            }
        }
    }
}