package org.example.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article
{
    String author, artName, preview;
    String[] tags;
    Date timeOfPublication;
    int views, readingTime;
    //Duration readingTime;
    URL picURL;

    @Override
    public String toString()
    {
        return author + "\n" +
                artName + "\n" +
                timeOfPublication.toString() + "\n" +
                readingTime + " мин\n" + // .toMinutes()
                "Просмотры: " + views + "\n" +
                "Тэги: " + Arrays.toString(tags) + "\n" +
                "PicURL: " + picURL + "\n" +
                preview + "\n";
    }

    public static void toGsonFile(List<Article> arts, String filename)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(filename))
        {
            gson.toJson(arts, writer);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
