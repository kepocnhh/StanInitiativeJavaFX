package stan.initiative.commander;

import java.util.List;

public class Mode
{
	public String name;
    private List<State> states;
	
	public Mode(String n)
	{
		name = n;
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