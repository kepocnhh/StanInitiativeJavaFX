package stan.initiative.telegram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class Telegram
{
    static private final String BASE_URL = "https://api.telegram.org";
	
    static private final String STICKER_FIST_ID = "BQADAgADeAcAAlOx9wOjY2jpAAHq9DUC";
	
    static private Telegram instance;
    static public Telegram getInstance()
    {
        if(instance == null)
        {
            instance = new Telegram();
        }
        return instance;
    }
	
	private Bot bot;
	
    private Telegram()
    {
	}
	
    public void setBot(int i, String t, int c)
    {
		this.bot = new Bot(i, t);
		this.bot.setChatIdMe(c);
	}
	
    public void sendBotMessageMe(String message)
    {
		sendBotMessage(bot.chatIdMe, message);
	}
    public void sendBotMessage(int chat_id, String message)
    {
        try
        {
			URLConnection urlConn = new URL(getUrlFromBot() + "/sendMessage?chat_id=" + chat_id + "&text=" + message).openConnection();
			urlConn.setUseCaches(false);
			System.out.println(inputDataFromUrlConnection(urlConn));
        }
        catch(IOException e)
        {
			System.out.println(e.getMessage());
        }
	}
	
    public void sendBotFistStickerMe()
    {
		sendBotStickerMe(STICKER_FIST_ID);
	}
    public void sendBotStickerMe(String stickerId)
    {
		sendBotSticker(bot.chatIdMe, stickerId);
	}
    public void sendBotSticker(int chat_id, String stickerId)
    {
        try
        {
			URLConnection urlConn = new URL(getUrlFromBot() + "/sendSticker?chat_id=" + chat_id + "&sticker=" + stickerId).openConnection();
			urlConn.setUseCaches(false);
			System.out.println(inputDataFromUrlConnection(urlConn));
        }
        catch(IOException e)
        {
			System.out.println(e.getMessage());
        }
	}
	
    public void getBotUpdates()
    {
        try
        {
			URLConnection urlConn = new URL(getUrlFromBot() + "/getUpdates").openConnection();
			urlConn.setUseCaches(false);
			System.out.println(inputDataFromUrlConnection(urlConn));
        }
        catch(IOException e)
        {
			System.out.println(e.getMessage());
        }
    }
	
    public String getUrlFromBot()
    {
		return BASE_URL + "/bot" + this.bot.id + ":" + this.bot.token;
	}
	
    private StringBuilder inputDataFromUrlConnection(URLConnection urlConn)
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