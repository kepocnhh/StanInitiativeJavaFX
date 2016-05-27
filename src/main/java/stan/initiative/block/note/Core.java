package stan.initiative.block.note;

import java.util.HashMap;

import stan.initiative.commander.Commander;
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
	}
}