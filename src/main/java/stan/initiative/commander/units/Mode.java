package stan.initiative.commander.units;

public class Mode
{
	public String name;
    public State[] states;

	public Mode(String n)
	{
		name = n;
	}
	public void setStates(State[] s)
	{
		states = s;
	}
	
	public boolean canFinish()
	{
		return true;
	}
	public void onPreExecute()
	{
	}
	public void onPostExecute()
	{
	}
}