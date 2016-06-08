package stan.initiative.telegram;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class PostBufferedImage
{
    static private final String LINE_FEED = "\r\n";
	
    private final String boundary;
    private URLConnection httpConn;
    private DataOutputStream output;

    public PostBufferedImage(String requestURL, BufferedImage p)
		throws IOException
	{
        this.boundary = "------" + System.currentTimeMillis();
        httpConn = new URL(requestURL).openConnection();
        httpConn.setDoOutput(true);
        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		output = new DataOutputStream(httpConn.getOutputStream());
		addBufferedImage(p);
    }

    public void addBufferedImage(BufferedImage p)
		throws IOException
	{
        output.writeBytes("--" + boundary);
		output.writeBytes(LINE_FEED);
        output.writeBytes("Content-Disposition: form-data; name=\"photo\"; filename=\"0\"");
		output.writeBytes(LINE_FEED);
        output.writeBytes(LINE_FEED);
		ImageIO.write(p, "png", output);
        output.writeBytes(LINE_FEED);
    }
    public URLConnection execute()
		throws IOException
	{
        output.writeBytes("--" + boundary + "--");
		output.writeBytes(LINE_FEED);
        output.close();
        return httpConn;
    }
}
