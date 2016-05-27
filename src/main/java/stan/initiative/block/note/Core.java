package stan.initiative.block.note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import stan.initiative.helpers.FileHelper;
import stan.initiative.helpers.json.JSONWriter;

public class Core
{
    static private Core instance;
    static public Core getInstance()
    {
        if(instance == null)
        {
            instance = new Core();
        }
        return instance;
    }

    private Core()
    {
    }
	
    public void createBlockNote(String path, String name)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(path + "/" + name + ".star");
        }
		catch(Exception e)
        {
			System.out.println("FileOutputStream - " + e.getMessage());
        }
		HashMap main = new HashMap<String, Object>();
		main.put("name", name);
		main.put("blocks", new Object[0]);
		main.put("tables", new Object[0]);
		String data = "";
        try
        {
            data = JSONWriter.mapToJSONString(main);
        }
		catch(Exception e)
        {
			System.out.println("mapToJSONString - " + e.getMessage());
        }
        try
        {
            FileHelper.writeFile(data, path + "/main");
        }
		catch(Exception e)
        {
			System.out.println("writeFile - " + e.getMessage());
        }
        ZipOutputStream zos = new ZipOutputStream(fos);
        try
        {
            addToZipFile(path + "/main", zos);
        } catch(IOException e)
        {
			System.out.println("addToZipFile - " + e.getMessage());
        }
        try
        {
            zos.close();
        } catch(IOException e)
        {
			System.out.println("zos.close - " + e.getMessage());
        }
	}
	
    private void addToZipFile(String fileName, ZipOutputStream zos)
            throws IOException
    {
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while((length = fis.read(bytes)) >= 0)
        {
            zos.write(bytes, 0, length);
        }
        zos.closeEntry();
        fis.close();
    }
}