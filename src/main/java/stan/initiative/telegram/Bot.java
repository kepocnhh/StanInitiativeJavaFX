package stan.initiative.telegram;

import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Bot
    extends Sender
    implements Consts
{
    public int chatIdMe;

    public Bot(int id, String token)
    {
    	super(BASE_URL + "/bot" + id + ":" + token);
    }

    public void setChatIdMe(int c)
    {
        chatIdMe = c;
    }

    public String sendPhotoMe(BufferedImage photo)
		throws IOException
    {
        return sendPhoto(this.chatIdMe, photo);
    }
    public String sendFistStickerMe()
    {
        return sendStickerMe(STICKER_FIST_ID);
    }
    public String sendStickerMe(String stickerId)
    {
        return sendSticker(this.chatIdMe, stickerId);
    }
}