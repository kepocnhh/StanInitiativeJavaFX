package stan.initiative.ui.controls.buttons.media.music;

import javafx.scene.control.Button;

public class MusicPlayerPlayButton
    extends Button
{
    public MusicPlayerPlayButton()
    {
        super();
        setPlayStyle();
    }

    public void setPlayStyle()
    {
        setId("mp_play_button");
    }
    public void setPauseStyle()
    {
        setId("mp_pause_button");
    }
}