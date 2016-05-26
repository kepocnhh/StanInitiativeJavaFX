package stan.initiative.commander;

import java.util.List;

public class State
{
	public String name;
    private List<Command> commands;

	public State(String n, List<Command> c)
	{
		name = n;
		commands = c;
	}
}