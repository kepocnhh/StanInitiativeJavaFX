package stan.initiative.commander;

public class Mode
{
	public String name;
    public State[] states;

	public Mode(String n, State[] s)
	{
		name = n;
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