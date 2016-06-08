package stan.initiative.telegram;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class CudgelBot
    extends Bot
{
    public CudgelBot(int id, String token)
    {
    	super(id, token);
    }
	
    public void sendPhotoMe(BufferedImage photo, IRequestListener l)
    {
		new Thread(new Runnable()
        {
            public void run()
            {
				try
				{
					l.answer(sendPhoto(CudgelBot.this.chatIdMe, photo));
				}
				catch(IOException e)
				{
					l.error(e);
				}
            }
        }).start();
    }
}