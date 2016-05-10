package stan.initiative.ui.scenes;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import stan.initiative.res.values.Strings;
import stan.initiative.ui.panes.MainPane;
import stan.initiative.ui.controllers.panes.MainPaneController;
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
	boolean dragged = false;
	Thread test;

    public MainScene(final Stage primaryStage)
    {
		super(new MainPane(), 200, 200);
		mainPane = (MainPane)this.getRoot();
		/*
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
		*/
        mainPane.startRecognize.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
				dragged = false;
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        mainPane.startRecognize.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
				dragged = true;
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
                if(al > 500)
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
				if(dragged)
				{
					return;
				}
				//voice.startRecognize();
				if(test != null)
				{
					if (test.isAlive())
					{
						test.stop();
						mainPane.startRecognize.setScaleX(1);
						mainPane.startRecognize.setScaleY(1);
						return;
					}
				}
				test = new Thread(new Runnable()
				{
					@Override public void run()
					{
						boolean up = true;
						int j = 0;
						for (int i=0; i<100; i++)
						{
							try
							{
								Thread.sleep(75);  
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
							final double size = ((double)j/40) + (double)1;
							Platform.runLater(new Runnable()
							{
								@Override public void run()
								{
									mainPane.startRecognize.setScaleX(size);
									mainPane.startRecognize.setScaleY(size);
								}
							});
							if(up)
							{
								j += (40 - j)/9 + 1;
							}
							else
							{
								j -= (40 - j)/9 + 1;
							}
							if(j >= 30)
							{
								up = false;
							}
							else if(j <= 0)
							{
								up = true;
							}
						}
						Platform.runLater(new Runnable()
						{
							@Override public void run()
							{
								mainPane.startRecognize.setScaleX(1);
								mainPane.startRecognize.setScaleY(1);
							}
						});
					}
				});
				test.start();
			}
		});
		mainPane.stopRecognize.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				mainPane.stopRecognize.setScaleX(1.5);
				mainPane.stopRecognize.setScaleY(1.5);
				//voice.stopRecognize();
			}
		});
    }
}