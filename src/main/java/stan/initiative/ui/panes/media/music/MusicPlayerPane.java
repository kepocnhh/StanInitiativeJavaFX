package stan.initiative.ui.panes.media.music;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import stan.initiative.media.music.Player;
import stan.initiative.ui.controls.buttons.media.music.MusicPlayerExitButton;
import stan.initiative.ui.controls.buttons.media.music.MusicPlayerPlayButton;
import stan.initiative.ui.controls.buttons.media.music.MusicPlayerNextButton;
import stan.initiative.listeners.ui.controls.buttons.IMouseEventButtonListener;
import stan.initiative.listeners.ui.controls.buttons.media.music.IExitButtonListener;
import stan.initiative.listeners.ui.panes.media.music.IMusicPlayerPaneListener;

public class MusicPlayerPane
    extends HBox
{
    private double xOffset = 0;
    private double yOffset = 0;

    //VIEWS
    public MusicPlayerExitButton exitButton;
    public MusicPlayerPlayButton playButton;
    public MusicPlayerNextButton nextButton;

    public MusicPlayerPane(final Stage primaryStage, IMusicPlayerPaneListener l)
    {
        super();
        this.setStyle("-fx-background-color: null");
        exitButton = new MusicPlayerExitButton(new IExitButtonListener()
        {
            public void exit()
            {
                Player.getInstance().stop();
                l.exit();
            }
        });
        exitButton.setMouseEventListener(new IMouseEventButtonListener()
        {
            public void mousePressed(MouseEvent event)
            {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
            public void mouseDragged(MouseEvent event)
            {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
        playButton = new MusicPlayerPlayButton();
        playButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(Player.getInstance().isPaused() || Player.getInstance().isStoped())
                {
                    Player.getInstance().play();
                }
                else
                {
                    Player.getInstance().pause();
                }
            }
        });
        nextButton = new MusicPlayerNextButton();
        nextButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                Player.getInstance().next();
            }
        });
        Player.getInstance().addListener(new Player.IMusicPlayerListener()
        {
            public void stop()
            {
                playButton.setPlayStyle();
            }
            public void pause()
            {
                playButton.setPlayStyle();
            }
            public void play()
            {
                playButton.setPauseStyle();
            }
        });
        System.out.println("MusicPlayerPane setting");
        VBox playVBox = new VBox();
        playVBox.setAlignment(Pos.CENTER);
        playVBox.getChildren().add(playButton);
        VBox nextVBox = new VBox();
        nextVBox.setAlignment(Pos.CENTER);
        nextVBox.getChildren().add(nextButton);
        VBox.setMargin(nextButton,new Insets(0,0,0,3));
        VBox exitVBox = new VBox();
        exitVBox.setAlignment(Pos.TOP_CENTER);
        exitVBox.getChildren().add(exitButton);
        VBox.setMargin(exitButton,new Insets(3,0,0,0));
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(playVBox, nextVBox, exitVBox);
    }
}