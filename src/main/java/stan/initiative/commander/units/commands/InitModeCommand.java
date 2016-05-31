package stan.initiative.commander.units.commands;

import stan.initiative.commander.units.Command;
import stan.initiative.commander.Commander;

public class InitModeCommand
    extends Command
{
	static public final String ID_KEY = "initMode";
	static public final String MODE_NUM_KEY = "mode";

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