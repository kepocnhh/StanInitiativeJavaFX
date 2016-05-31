package stan.initiative.commander.units.commands;

import stan.initiative.commander.units.Command;
import stan.initiative.commander.Commander;

public class InitStateCommand
    extends Command
{
	static public final String ID_KEY = "initState";
	static public final String STATE_NUM_KEY = "state";

	public int stateId;
	
	public InitStateCommand(String n, String[] k, int s)
	{
		super(n, k);
		stateId = s;
	}

	public void execute()
	{
		Commander.getInstance().addNewState(stateId);
	}
}