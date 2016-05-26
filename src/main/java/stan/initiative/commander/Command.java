package stan.initiative.commander;

public class Command
{
	public String name;
	private String[] keys;

	public Command(String n, String[] k)
	{
		name = n;
		keys = k;
	}

	public void execute()
	{
		
	}
}