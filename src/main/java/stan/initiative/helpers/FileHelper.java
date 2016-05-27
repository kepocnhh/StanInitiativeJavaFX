package stan.initiative.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FileHelper
{
    static public String readFile(String filename)
    {
        String everything = null;
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line != null)
            {
                sb.append(line);
                line = br.readLine();
                if(line != null)
                {
                    sb.append("\n");
                }
            }
            everything = sb.toString();
        }
        catch(Exception e)
        {
        	
        }
        finally
        {
            if(br != null)
            {
                try
                {
                    br.close();
                }
                catch(Exception e)
                {
                	
                }
            }
        }
        return everything;
    }
    static public void writeFile(String data, String filename)
    {
		BufferedWriter bw = null;
		try
		{
			bw = new BufferedWriter(new FileWriter(filename));
			bw.write(data);
		}
		catch(Exception e)
		{
		}
		finally
		{
            if(bw != null)
            {
                try
                {
                    bw.close();
                }
                catch(Exception e)
                {
                	
                }
            }
		}
	}
}