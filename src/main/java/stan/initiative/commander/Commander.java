package stan.initiative.commander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Commander
{
    static private Commander instance;
    static public Commander getInstance()
    {
        if(instance == null)
        {
            instance = new Commander();
        }
        return instance;
    }
	
	private int actualMode = -1;
    public List<Mode> modes;
    private List<State> states;
    private List<Command> commands;
	
    private Commander()
    {
	}
	
	public void initData(ArrayList m, ArrayList s, ArrayList c)
	{
		modes = new ArrayList<>();
		for(int i=0; i<m.size(); i++)
		{
			HashMap tmp = (HashMap)m.get(i);
			addMode(new Mode((String)tmp.get("name")));
		}
	}
	private void addMode(Mode m)
	{
		modes.add(m);
	}
	
	public void initNewMode(int newMode)
	{
		if(actualMode >= 0)
		{
			if(!modes.get(actualMode).canFinish())
			{
				return;
			}
			modes.get(actualMode).onPostExecute();
		}
		actualMode = newMode;
		modes.get(actualMode).onPreExecute();
	}
}