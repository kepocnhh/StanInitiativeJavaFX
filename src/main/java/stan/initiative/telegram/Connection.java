package stan.initiative.telegram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class Connection
{
    static public String sendMessage(String prefix, int chat_id, String message)
    {
        try
        {
            return inputDataFromUrlConnection(buildURLConnection(prefix + "/sendMessage?chat_id=" + chat_id + "&text=" + message)).toString();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    static public String sendSticker(String prefix, int chat_id, String stickerId)
    {
        try
        {
            return inputDataFromUrlConnection(buildURLConnection(prefix + "/sendSticker?chat_id=" + chat_id + "&sticker=" + stickerId)).toString();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    static public String getUpdates(String prefix)
    {
        try
        {
            return inputDataFromUrlConnection(buildURLConnection(prefix + "/getUpdates")).toString();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    static private URLConnection buildURLConnection(String url)
    	throws IOException
    {
        URLConnection urlConn = new URL(url).openConnection();
        urlConn.setUseCaches(false);
        return urlConn;
    }

    static private StringBuilder inputDataFromUrlConnection(URLConnection urlConn)
    	throws IOException
    {
        BufferedReader br = new BufferedReader(
            new InputStreamReader(urlConn.getInputStream(), Charset.forName("UTF-8")));
        StringBuilder completeResponse = new StringBuilder();
        String response = br.readLine();
        while(response != null)
        {
            completeResponse.append(response);
            response = br.readLine();
        }
        br.close();
        return completeResponse;
    }
}