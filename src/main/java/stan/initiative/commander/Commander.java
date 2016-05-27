package stan.initiative.commander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stan.initiative.commander.commands.FinishActualModeCommand;
import stan.initiative.commander.commands.FinishStateCommand;
import stan.initiative.commander.commands.InitModeCommand;
import stan.initiative.commander.commands.InitStateCommand;

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

    private int ids;
    private int actualMode;
    private List<Integer> actualStates;
    public Mode[] modes;
    public State[] states;
    public Command[] commands;
    private IExtraCommands extraCommands;

    private Commander()
    {
    	ids = 0;
    	actualMode = -1;
		actualStates = new ArrayList<>();
    }

    public void addExtraCommands(IExtraCommands e)
    {
    	extraCommands = e;
    }

    public void initData(ArrayList m, ArrayList s, ArrayList c)
    {
		modes = new Mode[m.size()];
        for(int i = 0; i < m.size(); i++)
        {
            HashMap tmp = (HashMap)m.get(i);
            String modeName = (String)tmp.get("name");
            State[] states = null;
            if(tmp.get("states") != null)
            {
				states = getArrayStates(i, (ArrayList)tmp.get("states"));
            }
			else
			{
				states = new State[0];
			}
            Mode mode = new Mode(modeName, states);
            modes[i] = mode;
        }
        states = getArrayStates(-1, s);
        commands = getArrayCommands(-1, c);
    }
    private State[] getArrayStates(int parentMode, ArrayList s)
    {
        State[] states = getArrayStates(s);
        for(int i = 0; i < s.size(); i++)
        {
            HashMap tmp = (HashMap)s.get(i);
            Command[] commands = null;
            if(tmp.get("commands") != null)
            {
				commands = getArrayCommands(parentMode, (ArrayList)tmp.get("commands"));
            }
			else
			{
				commands = new Command[0];
			}
            states[i].setCommands(commands);
        }
        return states;
    }
    private State[] getArrayStates(ArrayList s)
    {
        State[] states = new State[s.size()];
        for(int i = 0; i < s.size(); i++)
        {
            HashMap tmp = (HashMap)s.get(i);
            String stateName = (String)tmp.get("name");
            states[i] = new State(getNewId(), stateName);
        }
        return states;
	}
    private Command[] getArrayCommands(int parentMode, ArrayList c)
    {
        Command[] commands = new Command[c.size()];
        for(int i = 0; i < c.size(); i++)
        {
            HashMap tmp = (HashMap)c.get(i);
            Command command = getCommand(parentMode, tmp);
            if(command == null)
            {
                continue;
            }
            commands[i] = command;
        }
        return commands;
    }
    private Command getCommand(int parentMode, HashMap c)
    {
        Command command = null;
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
        if(c.get("initMode") != null)
        {
            HashMap initMode = (HashMap)c.get("initMode");
            command = new InitModeCommand(commandName, keys, ((Long)initMode.get("mode")).intValue());
        }
        else if(c.get("initState") != null)
        {
            HashMap initState = (HashMap)c.get("initState");
			int stateId = -1;
			int stateN = ((Long)initState.get("state")).intValue();
			if(parentMode < 0)
			{
				stateId = states[stateN].id;
			}
			else
			{
				stateId = modes[parentMode].states[stateN].id;
			}
            command = new InitStateCommand(commandName, keys, stateId);
		}
        else if(c.get("finishThisMode") != null)
        {
            command = new FinishActualModeCommand(commandName, keys);
        }
        else if(c.get("finishState") != null)
        {
            HashMap finishState = (HashMap)c.get("finishState");
			int stateId = -1;
			int stateN = ((Long)finishState.get("state")).intValue();
			if(parentMode < 0)
			{
				stateId = states[stateN].id;
			}
			else
			{
				stateId = modes[parentMode].states[stateN].id;
			}
            command = new FinishStateCommand(commandName, keys, stateId);
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
		if(tryExecuteCommand(modes[actualMode].states, key))
		{
			return;
		}
		if(tryExecuteCommand(states, key))
		{
			return;
		}
		if(tryExecuteCommand(commands, key))
		{
			return;
		}
    }
    private boolean tryExecuteCommand(State[] states, String key)
    {
		for(int i=0; i<states.length; i++)
		{
			if(states[i].isActive())
			{
				if(tryExecuteCommand(states[i].commands, key))
				{
					return true;
				}
			}
		}
		return false;
	}
    private boolean tryExecuteCommand(Command[] commands, String key)
    {
		for(int i=0; i<commands.length; i++)
		{
			for(int j=0; j<commands[i].keys.length; j++)
			{
				if(key.contains(commands[i].keys[j]))
				{
					commands[i].execute();
					return true;
				}
			}
		}
		return false;
	}

    public void addNewState(int newState)
    {
		State state = null;
		state = getState(modes[actualMode].states, newState);
		if(state != null)
		{
			if(!state.isActive())
			{
				state.activate();
			}
			return;
		}
		state = getState(states, newState);
		if(state != null)
		{
			if(!state.isActive())
			{
				state.activate();
			}
			return;
		}
	}
    public State getState(State[] states, int state)
    {
		for(int i=0; i<states.length; i++)
		{
			if(states[i].id == state)
			{
				return states[i];
			}
		}
		return null;
	}
    public void finishState(int fState)
    {
		State state = null;
		state = getState(modes[actualMode].states, fState);
		if(state != null)
		{
			if(state.isActive())
			{
				state.deActivate();
			}
			return;
		}
		state = getState(states, fState);
		if(state != null)
		{
			if(state.isActive())
			{
				state.deActivate();
			}
			return;
		}
	}
	
    public void initNewMode(int newMode)
    {
        if(actualMode >= 0)
        {
            if(actualMode == newMode || !modes[actualMode].canFinish())
            {
                return;
            }
            modes[actualMode].onPostExecute();
        }
        actualMode = newMode;
        modes[actualMode].onPreExecute();
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
        if(!modes[actualMode].canFinish())
        {
            return;
        }
        modes[actualMode].onPostExecute();
    }
	
    private int getNewId()
    {
		ids++;
		return ids;
	}
}