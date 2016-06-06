package stan.initiative.media.images;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.util.Date;
import javax.imageio.ImageIO;

public class ScreenShot
{
    static private ScreenShot instance;
    static public ScreenShot getInstance()
    {
        if(instance == null)
        {
            instance = new ScreenShot();
        }
        return instance;
    }

    private String path;

    private ScreenShot()
    {
    }

    public void setPath(String p)
    {
        this.path = p;
    }

    public BufferedImage grabScreen(int x, int y, int width, int height)
    {
        try
        {
            return new Robot().createScreenCapture(new Rectangle(x, y, width, height));
        }
        catch(Exception e)
        {
        }
        return null;
    }
    public byte[] grabScreenBytes(int x, int y, int width, int height)
    {
    	DataBuffer db = grabScreen(x, y, width, height).getRaster().getDataBuffer();
    	if(db instanceof DataBufferByte)
    	{
    		return ((DataBufferByte)db).getData();
    	}
    	else if(db instanceof DataBufferInt)
    	{
    		return convert((DataBufferInt)db);
    	}
        return null;
    }
    private byte[] convert(DataBufferInt dbi)
    {
        int[] ints = dbi.getData();
        byte[] data = new byte[ints.length];
        for(int i = 0; i < ints.length; i++)
        {
            data[i] = (byte)ints[i];
        }
        return data;
    }
    public void saveScreen(BufferedImage bi)
    {
        saveScreen(bi, this.path, new Date().getTime() + "");
    }
    public void saveScreen(BufferedImage bi, String path, String name)
    {
        try
        {
            ImageIO.write(bi, "png", new File(path + "/" + name + ".png"));
        }
        catch(Exception e)
        {
        }
    }
}