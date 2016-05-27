package stan.initiative.commander.commands;

import stan.initiative.commander.Command;
import stan.initiative.commander.Commander;

public class InitModeCommand
    extends Command
{
	public int mode;
	
	public InitModeCommand(String n, String[] k, int m)
	{
		super(n, k);
		mode = m;
	}

	public void execute()
	{
		Commander.getInstance().initNewMode(mode);
	}
}