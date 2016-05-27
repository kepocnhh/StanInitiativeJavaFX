package stan.initiative.commander.commands;

import stan.initiative.commander.Command;
import stan.initiative.commander.Commander;

public class InitStateCommand
    extends Command
{
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