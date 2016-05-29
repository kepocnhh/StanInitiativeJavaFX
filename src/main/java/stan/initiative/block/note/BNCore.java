package stan.initiative.block.note;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import stan.initiative.helpers.FileHelper;
import stan.initiative.helpers.json.JSONWriter;
import stan.initiative.helpers.json.JSONParser;

public class BNCore
{
    static private final String extension = "star";
    static private final String mainFile = "main";

    static private BNCore instance;
    static public BNCore getInstance()
    {
        if(instance == null)
        {
            instance = new BNCore();
        }
        return instance;
    }

    private String path;
    private HashMap main;
    private List<Integer> idTree;

    private BNCore()
    {
    }

    public Block getActualBlock()
    {
        Block block = null;
        HashMap map = getActualHashMap();
        if(map != null)
        {
            block = getBlockFromMap(map);
            block.blocks = new ArrayList<>();
            ArrayList blocks = (ArrayList)map.get("blocks");
            for(int i = 0; i < blocks.size(); i++)
            {
                block.blocks.add(getBlockFromMap((HashMap)blocks.get(i)));
            }
        }
        return block;
    }
    public Block getBlockFromMap(HashMap map)
    {
        HashMap datesMap = (HashMap)map.get("dates");
        Dates dates = new Dates((Long)datesMap.get("create"));
        dates.update = (Long)datesMap.get("update");
        dates.sync = (Long)datesMap.get("sync");
        return new Block((String)map.get("id"), (String)map.get("name"), dates);
    }
    private HashMap getActualHashMap()
    {
        ArrayList blocks = null;
        HashMap block = main;
        for(int i = 0; i < idTree.size(); i++)
        {
            blocks = (ArrayList)block.get("blocks");
            if(blocks != null && blocks.size() > 0 && blocks.size() <= idTree.get(i))
            {
                block = getBlock(blocks, idTree.get(i));
            }
            else
            {
                return null;
            }
        }
        return block;
    }
    private HashMap getBlock(ArrayList blocks, String id)
    {
        for(int i = 0; i < blocks.size(); i++)
        {
            HashMap block = (HashMap)blocks.get(i);
            if(block != null && block.get("id").equals(id))
            {
                return block;
            }
        }
        return null;
    }
    private HashMap getBlock(ArrayList blocks, int num)
    {
        return (HashMap)blocks.get(num);
    }

    private void updateDates()
    {
        updateDates(main, 0);
    }
    private void updateDates(HashMap block, int next)
    {
        HashMap dates = (HashMap)block.get("dates");
        if(next + 1 < idTree.size())
        {
            ArrayList blocks = (ArrayList)block.get("blocks");
            updateDates((HashMap)blocks.get(idTree.get(next)), next + 1);
        }
        dates.put("update", new Date().getTime());
    }

    private HashMap createEmptyBlock(String name)
    {
        long create = new Date().getTime();
        HashMap map = new HashMap<String, Object>();
        map.put("id", UUID.randomUUID().toString());
        map.put("name", name);
        map.put("blocks", new Object[0]);
        map.put("tables", new Object[0]);
        HashMap dates = new HashMap<String, Object>();
        dates.put("create", create);
        dates.put("update", create);
        dates.put("sync", -1);
        map.put("dates", dates);
        return map;
    }


    public void openBlockNote(String fullPath)
    {
        ZipFile zf = null;
        try
        {
            zf = new ZipFile(fullPath);
        }
        catch(Exception e)
        {
            System.out.println("ZipFile - " + e.getMessage());
        }
        ZipEntry entry = getZipEntryFromName(zf, "main");
        try
        {
            main = getMapFromString(FileHelper.readFile(zf.getInputStream(entry)));
        }
        catch(Exception e)
        {
            System.out.println("getMapFromString - " + e.getMessage());
        }
        path = fullPath;
        idTree = new ArrayList<>();
    }
    private ZipEntry getZipEntryFromName(ZipFile zf, String name)
    {
        for(Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements();)
        {
            ZipEntry entry = e.nextElement();
            if(entry.getName().equals(name))
            {
                return entry;
            }
        }
        return null;
    }
    private ZipEntry getZipEntryFromName(ZipInputStream zis, String name)
    throws IOException
    {
        ZipEntry entry = zis.getNextEntry();
        while(entry != null)
        {
            if(entry.getName().equals(name))
            {
                return entry;
            }
            entry = zis.getNextEntry();
        }
        return null;
    }
    private HashMap getMapFromString(String result)
    {
        JSONParser parser = new JSONParser();
        try
        {
            return (HashMap)parser.parse(result);
        }
        catch(Exception e)
        {
            System.out.println("JSONParser - " + e.getMessage());
        }
        return null;
    }
    public void createBlockNote(String path, String name)
    {
        String data = null;
        try
        {
            data = JSONWriter.mapToJSONString(createEmptyBlock(name));
        }
        catch(Exception e)
        {
            System.out.println("mapToJSONString - " + e.getMessage());
            return;
        }
        File main = new File(path + "/main");
        try
        {
            FileHelper.writeFile(data, path + "/main");
        }
        catch(Exception e)
        {
            System.out.println("writeFile - " + e.getMessage());
            return;
        }
        ZipOutputStream zos = null;
        try
        {
            zos = new ZipOutputStream(new FileOutputStream(path + "/" + name + "." + extension));
        }
        catch(Exception e)
        {
            System.out.println("ZipOutputStream - " + e.getMessage());
            return;
        }
        try
        {
            addFileToZip(main, zos);
        }
        catch(IOException e)
        {
            System.out.println("addFileToZip - " + e.getMessage());
            return;
        }
        main.delete();
        try
        {
            zos.close();
        }
        catch(IOException e)
        {
            System.out.println("zos.close - " + e.getMessage());
            return;
        }
    }

    public void updateBlockNote()
    {
        String data = null;
        try
        {
            data = JSONWriter.mapToJSONString(main);
        }
        catch(Exception e)
        {
            System.out.println("mapToJSONString - " + e.getMessage());
            return;
        }
        updateBlockNote("main", data);
    }
    public void putNewBlock()
    {
        HashMap map = getActualHashMap();
        if(map == null)
        {
            return;
        }
        ArrayList blocks = (ArrayList)map.get("blocks");
        blocks.add(createEmptyBlock("new Block"));
        updateDates();
        updateBlockNote();
    }

    private void addFileToZip(String fileName, ZipOutputStream zos)
    throws IOException
    {
        File file = new File(fileName);
        addFileToZip(file, zos);
    }
    private void addFileToZip(File file, ZipOutputStream zos)
    throws IOException
    {
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);
        writeData(zos, fis);
        zos.closeEntry();
        fis.close();
    }
    private void writeData(ZipOutputStream zos, InputStream is)
    throws IOException
    {
        byte[] bytes = new byte[1024];
        int length;
        while((length = is.read(bytes)) >= 0)
        {
            zos.write(bytes, 0, length);
        }
    }
    private void updateBlockNote(String name, String data)
    {
        ZipOutputStream zos = null;
        try
        {
            zos = new ZipOutputStream(new FileOutputStream(path));
        }
        catch(Exception e)
        {
            System.out.println("ZipOutputStream - " + e.getMessage());
            return;
        }
        try
        {
            writeDataToZip(zos, name, data);
        }
        catch(Exception e)
        {
            System.out.println("writeDataToZip - " + e.getMessage());
        }
    }
    private void writeDataToZip(ZipOutputStream zos, String name, String data)
    throws IOException
    {
        zos.putNextEntry(new ZipEntry(name));
        InputStream is = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        writeData(zos, is);
        is.close();
        zos.closeEntry();
        zos.close();
    }
}