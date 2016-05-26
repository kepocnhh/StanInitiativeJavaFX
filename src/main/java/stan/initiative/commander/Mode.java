package stan.initiative.commander;

import java.util.List;

public class Mode
{
	public String name;
    private List<State> states;

	public Mode(String n, List<State> s)
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