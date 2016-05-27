package stan.initiative.commander;

public abstract class Unit
{
	public String name;
	public int id;

	public Unit(int i, String n)
	{
		id = i;
		name = n;
	}
}