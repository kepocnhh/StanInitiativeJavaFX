package stan.initiative.commander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stan.initiative.commander.commands.FinishActualModeCommand;
import stan.initiative.commander.commands.InitModeCommand;

public class Commander
{
    static private Commander instance;
    static public Commander getInstance()
    {
        if(instance == null)
        {
            instance = new Commander();
        }
        return instance;
    }

    private int actualMode;
    public List<Mode> modes;
    public List<State> states;
    public List<Command> commands;
    private IExtraCommands extraCommands;

    private Commander()
    {
    	actualMode = -1;
    }

    public void addExtraCommands(IExtraCommands e)
    {
    	extraCommands = e;
    }

    public void initData(ArrayList m, ArrayList s, ArrayList c)
    {
        modes = new ArrayList<>();
        for(int i = 0; i < m.size(); i++)
        {
            HashMap tmp = (HashMap)m.get(i);
            String modeName = (String)tmp.get("name");
            if(tmp.get("states") == null)
            {
                continue;
            }
            List<State> states = getListStates((ArrayList)tmp.get("states"));
            Mode mode = new Mode(modeName, states);
            modes.add(mode);
        }
        states = getListStates(s);
        commands = getListCommands(c);
    }
    private List<State> getListStates(ArrayList s)
    {
        List<State> states = new ArrayList<>();
        for(int i = 0; i < s.size(); i++)
        {
            HashMap tmp = (HashMap)s.get(i);
            String stateName = (String)tmp.get("name");
            if(tmp.get("commands") == null)
            {
                continue;
            }
            List<Command> commands = getListCommands((ArrayList)tmp.get("commands"));
            states.add(new State(stateName, commands));
        }
        return states;
    }
    private List<Command> getListCommands(ArrayList c)
    {
        List<Command> commands = new ArrayList<>();
        for(int i = 0; i < c.size(); i++)
        {
            HashMap tmp = (HashMap)c.get(i);
            Command command = getCommand(tmp);
            if(command == null)
            {
                continue;
            }
            commands.add(command);
        }
        return commands;
    }
    private Command getCommand(HashMap c)
    {
        Command command = null;
        String commandName = (String)c.get("name");
        String[] keys = null;
        if(c.get("keys") != null)
        {
            keys = getArrayKeys((ArrayList)c.get("keys"));
        }
        if(c.get("initMode") != null)
        {
            HashMap initMode = (HashMap)c.get("initMode");
            command = new InitModeCommand(commandName, keys, ((Long)initMode.get("mode")).intValue());
        }
        else if(c.get("finishThisMode") != null)
        {
            command = new FinishActualModeCommand(commandName, keys);
        }
        if(extraCommands != null)
        {
        	command = extraCommands.getExtraCommand(c);
        }
        return command;
    }
    private String[] getArrayKeys(ArrayList k)
    {
        String[] keys = new String[k.size()];
        for(int i = 0; i < k.size(); i++)
        {
            String key = (String)k.get(i);
            keys[i] = key;
        }
        return keys;
    }

    public void parseKey(String key)
    {

    }

    public void initNewMode(int newMode)
    {
        if(actualMode >= 0)
        {
            if(actualMode == newMode || !modes.get(actualMode).canFinish())
            {
                return;
            }
            modes.get(actualMode).onPostExecute();
        }
        actualMode = newMode;
        modes.get(actualMode).onPreExecute();
    }
    public void finishMode(int fMode)
    {
        if(actualMode != fMode)
        {
            return;
        }
        finishActualMode();
    }
    public void finishActualMode()
    {
        if(!modes.get(actualMode).canFinish())
        {
            return;
        }
        modes.get(actualMode).onPostExecute();
    }
}