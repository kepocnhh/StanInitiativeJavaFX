package stan.initiative.commander.units.commands;

import stan.initiative.commander.units.Command;
import stan.initiative.commander.Commander;

public class FinishActualModeCommand
    extends Command
{
	public FinishActualModeCommand(String n, String[] k)
	{
		super(n, k);
	}

	public void execute()
	{
		Commander.getInstance().finishActualMode();
	}
}