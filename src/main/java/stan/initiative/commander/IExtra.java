package stan.initiative.commander;

import java.util.HashMap;

import stan.initiative.commander.units.Command;
import stan.initiative.commander.units.Mode;
import stan.initiative.commander.units.State;

public interface IExtra
{
	Command getExtraCommand(HashMap c);
	State getExtraState(HashMap s);
	Mode getExtraMode(HashMap m);
}