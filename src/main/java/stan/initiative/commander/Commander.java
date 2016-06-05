package stan.initiative.commander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stan.initiative.commander.units.Command;
import stan.initiative.commander.units.Mode;
import stan.initiative.commander.units.State;
import stan.initiative.commander.units.commands.FinishActualModeCommand;
import stan.initiative.commander.units.commands.FinishStateCommand;
import stan.initiative.commander.units.commands.InitModeCommand;
import stan.initiative.commander.units.commands.InitStateCommand;

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
    private IExtra extra;

    private Commander()
    {
        ids = 0;
        actualMode = -1;
        actualStates = new ArrayList<>();
    }

    public void addExtra(IExtra e)
    {
        extra = e;
    }

    public void initData(ArrayList m, ArrayList s, ArrayList c)
    {
        this.modes = getArrayModes(m);
        System.out.println("Command - initData - getArrayModes");
        this.states = getArrayStates(s);
        System.out.println("Command - initData - getArrayStates");
        commands = getArrayCommands(-1, c);
        System.out.println("Command - initData - getArrayCommands");
        setModesStates(this.modes, m);
        System.out.println("Command - initData - setModesStates");
        setStatesCommands(this.states, s, -1);
        System.out.println("Command - initData - setStatesCommands");
    }
    private Mode[] getArrayModes(ArrayList m)
    {
        Mode[] modes = new Mode[m.size()];
        for(int i = 0; i < m.size(); i++)
        {
            HashMap tmp = (HashMap)m.get(i);
            if(extra != null)
            {
                modes[i] = extra.getExtraMode(tmp);
            }
            if(modes[i] == null)
            {
                modes[i] = new Mode((String)tmp.get("name"));
            }
        }
        return modes;
    }
    private void setModesStates(Mode[] modes, ArrayList m)
    {
        for(int i = 0; i < m.size(); i++)
        {
            HashMap tmp = (HashMap)m.get(i);
            State[] modeStates = null;
            if(tmp.get("states") != null)
            {
                ArrayList ss = (ArrayList)tmp.get("states");
                modeStates = getArrayStates(ss);
                setStatesCommands(modeStates, ss, i);
            }
            else
            {
                modeStates = new State[0];
            }
            modes[i].setStates(modeStates);
        }
    }
    private State[] getArrayStates(ArrayList s)
    {
        State[] states = new State[s.size()];
        for(int i = 0; i < s.size(); i++)
        {
            HashMap tmp = (HashMap)s.get(i);
            if(extra != null)
            {
                tmp.put("id", getNewId());
                states[i] = extra.getExtraState(tmp);
            }
            if(states[i] == null)
            {
                states[i] = new State(getNewId(), (String)tmp.get("name"));
            }
        }
        return states;
    }
    private void setStatesCommands(State[] states, ArrayList s, int parentMode)
    {
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
        if(extra != null)
        {
            command = extra.getExtraCommand(c);
        }
        if(command != null)
        {
            return command;
        }
        else if(c.get(InitModeCommand.ID_KEY) != null)
        {
            HashMap initMode = (HashMap)c.get(InitModeCommand.ID_KEY);
            command = new InitModeCommand(commandName, keys, ((Long)initMode.get(InitModeCommand.MODE_NUM_KEY)).intValue());
        }
        else if(c.get(InitStateCommand.ID_KEY) != null)
        {
            HashMap initState = (HashMap)c.get(InitStateCommand.ID_KEY);
            int stateId = -1;
            int stateN = ((Long)initState.get(InitStateCommand.STATE_NUM_KEY)).intValue();
            if(parentMode < 0)
            {
                stateId = this.states[stateN].id;
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
                stateId = this.states[stateN].id;
            }
            else
            {
                stateId = modes[parentMode].states[stateN].id;
            }
            command = new FinishStateCommand(commandName, keys, stateId);
        }
        if(command == null)
        {
            command = new Command(commandName, keys);
        }
        return command;
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

    public void parseKey(String key)
    {
        if(actualMode > -1 && tryExecuteCommand(modes[actualMode].states, key))
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
        for(int i = 0; i < states.length; i++)
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
        for(int i = 0; i < commands.length; i++)
        {
            for(int j = 0; j < commands[i].keys.length; j++)
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
        if(actualMode >= 0)
        {
            state = getState(modes[actualMode].states, newState);
        }
        if(state == null)
        {
            state = getState(states, newState);
        }
        if(state != null)
        {
            if(!state.isActive())
            {
                state.activate();
                state.onPreExecute();
            }
            return;
        }
    }
    private State getState(State[] states, int state)
    {
        for(int i = 0; i < states.length; i++)
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
        if(actualMode >= 0)
        {
            state = getState(modes[actualMode].states, fState);
        }
        if(state == null)
        {
            state = getState(states, fState);
        }
        if(state != null)
        {
            if(state.isActive())
            {
                state.deActivate();
                state.onPostExecute();
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