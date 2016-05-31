package stan.initiative.commander.units.states;

import stan.initiative.commander.units.State;

public class TelegramState
	extends State
{
	static public final String ID_KEY = "telegram";

	public TelegramState(int i, String n)
	{
		super(i, n);
	}
	
	public void onPreExecute()
	{
		System.out.println("TelegramState - onPreExecute");
	}
	public void onPostExecute()
	{
		System.out.println("TelegramState - onPostExecute");
	}
}