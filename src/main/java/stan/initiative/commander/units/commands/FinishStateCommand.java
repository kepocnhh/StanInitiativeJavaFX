package stan.initiative.commander.units.commands;

import stan.initiative.commander.units.Command;
import stan.initiative.commander.Commander;

public class FinishStateCommand
    extends Command
{
	public int stateId;
	
	public FinishStateCommand(String n, String[] k, int s)
	{
		super(n, k);
		stateId = s;
	}

	public void execute()
	{
		Commander.getInstance().finishState(stateId);
	}
}