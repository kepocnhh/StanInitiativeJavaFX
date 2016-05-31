package stan.initiative.commander.units;

import stan.initiative.commander.Unit;

public class State
	extends Unit
{
    public Command[] commands;
    private boolean active;

	public State(int i, String n)
	{
		super(i, n);
		active = false;
	}
	public void setCommands(Command[] c)
	{
		commands = c;
	}
	
	public boolean isActive()
	{
		return active;
	}
	public void deActivate()
	{
		active = false;
	}
	public void activate()
	{
		active = true;
	}
	
	public void onPreExecute()
	{
	}
	public void onPostExecute()
	{
	}
}