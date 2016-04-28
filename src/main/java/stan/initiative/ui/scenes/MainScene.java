package stan.initiative.ui.scenes;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import stan.initiative.res.values.Strings;
import stan.initiative.ui.panes.MainPane;
import stan.initiative.helpers.GoogleSpeechApiHelper;

import stan.voice.recognition.Voice;
import stan.voice.recognition.google.response.GoogleResponse;

public class MainScene
    extends Scene
{
	private MainPane mainPane;
    private double xOffset = 0;
    private double yOffset = 0;

    private Voice voice;

	public MainScene(final Stage primaryStage)
	{
		super(new MainPane(), 200, 200);
		mainPane = (MainPane) this.getRoot();
        this.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        this.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
		init();
	}
	private void init()
	{
		voice = new Voice(new Voice.IResponseListener()
		{
			@Override
	        public void getSpeech(GoogleResponse deserialized)
	        {

	        }
			@Override
	        public void audioLevel(int al)
	        {
	        	if(al>500)
	        	{
					mainPane.audioLevel.setText("al - " + al);
	        	}
	        }
		}, GoogleSpeechApiHelper.API_KEY);
		mainPane.startRecognize.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
            	voice.startRecognize();
            }
        });
		mainPane.stopRecognize.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
            	voice.stopRecognize();
            }
        });
	}
}