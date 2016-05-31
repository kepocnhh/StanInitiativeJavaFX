package stan.initiative.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;

public class FileHelper
{
    static public String readFile(String filename)
    {
        try
        {
            return readFile(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
        }
        catch(Exception e)
        {
        }
        return null;
    }
    static public String readFile(InputStream is)
    {
        return readFile(new InputStreamReader(is));
    }
    static private String readFile(InputStreamReader isr)
    {
        String everything = null;
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(isr);
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
        writeFile(data, new File(filename));
    }
    static public void writeFile(String data, File file)
    {
        BufferedWriter bw = null;
        try
        {
            bw = new BufferedWriter(new FileWriter(file));
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