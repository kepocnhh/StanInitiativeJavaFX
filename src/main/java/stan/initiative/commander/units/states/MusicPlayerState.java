package stan.initiative.commander.units.states;

import stan.initiative.commander.units.State;
import stan.initiative.media.music.Player;
import stan.initiative.ui.stages.MusicPlayerStage;

public class MusicPlayerState
	extends State
{
	static public final String ID_KEY = "music_player";

	public MusicPlayerState(int i, String n)
	{
		super(i, n);
	}
	
	public void onPreExecute()
	{
		MusicPlayerStage.getInstance().showMusicPlayer();
	}
	public void onPostExecute()
	{
		Player.getInstance().stop();
		MusicPlayerStage.getInstance().hideMusicPlayer();
	}
}