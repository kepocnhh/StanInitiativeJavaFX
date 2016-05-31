package stan.initiative.commander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stan.initiative.commander.units.Command;
import stan.initiative.commander.units.Mode;
import stan.initiative.commander.units.State;
import stan.initiative.commander.units.states.TelegramState;

public class Controller
    implements IExtra
{
    public Command getExtraCommand(HashMap c)
    {
        return null;
    }

    public State getExtraState(HashMap s)
    {
        int id = (Integer)s.get("id");
        String name = (String)s.get("name");
        if(name.equals(TelegramState.ID_KEY))
        {
            return new TelegramState(id, name);
        }
        return null;
    }

    public Mode getExtraMode(HashMap m)
    {
        return null;
    }
}