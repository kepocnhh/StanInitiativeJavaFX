package stan.initiative.telegram;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
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

    static public String sendPhoto(String prefix, int chat_id, byte[] photo)
    {
        try
        {
            return inputDataFromUrlConnection(buildURLConnectionPost(prefix + "/sendPhoto?chat_id=" + chat_id, photo)).toString();
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

    static private URLConnection buildURLConnectionPost(String url, byte[] buffer)
    throws IOException
    {
        String attachmentName = "bitmap";
        String attachmentFileName = "bitmap.bmp";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        HttpURLConnection urlConn = (HttpURLConnection)buildURLConnection(url);
        System.out.println("Connection buildURLConnection");
        urlConn.setDoOutput(true);
        System.out.println("Connection setDoOutput");
        urlConn.setRequestMethod("POST");

        //urlConn.setRequestProperty("Connection", "Keep-Alive");
        urlConn.setRequestProperty("Cache-Control", "no-cache");
        urlConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        //urlConn.setRequestProperty("photo", "photo");
        System.out.println("Connection setRequestProperty");

        DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
        System.out.println("Connection getOutputStream");

        dos.writeBytes(twoHyphens + boundary + crlf);
        dos.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\"\"" + crlf);
        dos.writeBytes(crlf);
        System.out.println("Connection writeBytes");

        dos.write(buffer);
        System.out.println("Connection write");
        dos.writeBytes(crlf);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
        dos.flush();
        dos.close();
        System.out.println("Connection close");
        return urlConn;
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