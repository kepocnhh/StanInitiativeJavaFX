package stan.initiative.commander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stan.initiative.commander.units.Command;
import stan.initiative.commander.units.Mode;
import stan.initiative.commander.units.State;
import stan.initiative.commander.units.commands.media.music.MusicPlayerNextCommand;
import stan.initiative.commander.units.states.MusicPlayerState;
import stan.initiative.commander.units.states.TelegramState;

public class Controller
    implements IExtra
{
    static public HashMap<String, Integer> extraStates = new HashMap<String, Integer>();

    public Command getExtraCommand(HashMap c)
    {
        String commandName = (String)c.get("name");
        String[] keys = null;
        if(c.get("keys") != null)
        {
            keys = getArrayKeys((ArrayList)c.get("keys"));
        }
        else
        {
            keys = new String[0];
        }
        if(commandName.equals(MusicPlayerNextCommand.ID_KEY))
        {
            return new MusicPlayerNextCommand(commandName, keys);
        }
        return null;
    }

    public State getExtraState(HashMap s)
    {
        int id = (Integer)s.get("id");
        String name = (String)s.get("name");
        if(name.equals(TelegramState.ID_KEY))
        {
            extraStates.put(TelegramState.ID_KEY, id);
            return new TelegramState(id, name);
        }
        else if(name.equals(MusicPlayerState.ID_KEY))
        {
            extraStates.put(MusicPlayerState.ID_KEY, id);
            System.out.println("Controller extraStates MusicPlayerState - " + id);
            return new MusicPlayerState(id, name);
        }
        return null;
    }

    public Mode getExtraMode(HashMap m)
    {
        return null;
    }

    private String[] getArrayKeys(ArrayList k)
    {
        String[] keys = new String[k.size()];
        for(int i = 0; i < k.size(); i++)
        {
            String key = (String)k.get(i);
            keys[i] = key.toLowerCase();
        }
        return keys;
    }
}