package stan.initiative.ui.stages;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import stan.initiative.commander.Commander;
import stan.initiative.commander.Controller;
import stan.initiative.commander.units.states.MusicPlayerState;
import stan.initiative.listeners.ui.panes.media.music.IMusicPlayerPaneListener;
import stan.initiative.ui.panes.media.music.MusicPlayerPane;

public class MusicPlayerStage
    extends Stage
{
    static private MusicPlayerStage instance;
    static public MusicPlayerStage getInstance()
    {
        if(instance == null)
        {
            instance = new MusicPlayerStage();
        }
        return instance;
    }

    private MusicPlayerStage()
    {
        super();
        this.setAlwaysOnTop(true);
        this.setScene(new Scene(new MusicPlayerPane(this, new IMusicPlayerPaneListener()
        {
            public void exit()
            {
                if(Controller.extraStates.get(MusicPlayerState.ID_KEY) != null)
                {
                    Commander.getInstance().finishState(Controller.extraStates.get(MusicPlayerState.ID_KEY));
                }
            }
        }), 48 * 3, 48 + 48 / 2, Color.TRANSPARENT));
        this.initStyle(StageStyle.TRANSPARENT);
        this.getScene().getStylesheets().add("css/StanTheme.css");
    }

    public void hideMusicPlayer()
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                if(MusicPlayerStage.getInstance().isShowing())
                {
                    MusicPlayerStage.getInstance().hide();
                }
            }
        });
    }
    public void showMusicPlayer()
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                if(MusicPlayerStage.getInstance().isShowing())
                {
                    return;
                }
                MusicPlayerStage.getInstance().show();
            }
        });
    }
}