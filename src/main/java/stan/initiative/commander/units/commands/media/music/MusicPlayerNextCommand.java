package stan.initiative.commander.units.commands.media.music;

import stan.initiative.commander.units.Command;
import stan.initiative.media.music.Player;

public class MusicPlayerNextCommand
    extends Command
{
	static public final String ID_KEY = "music_player_next";

	public MusicPlayerNextCommand(String n, String[] k)
	{
		super(n, k);
	}

	public void execute()
	{
		Player.getInstance().next();
	}
}