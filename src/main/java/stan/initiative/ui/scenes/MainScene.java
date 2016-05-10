package stan.initiative.ui.scenes;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;

import stan.initiative.res.values.Strings;
import stan.initiative.ui.controls.buttons.VoiceRecognitionButton;
import stan.initiative.ui.panes.VoiceRecognitionPane;
import stan.initiative.helpers.GoogleSpeechApiHelper;

import stan.voice.recognition.Voice;
import stan.voice.recognition.google.response.GoogleResponse;

public class MainScene
    extends Scene
{
    private VoiceRecognitionPane mainPane;

    private Voice voice;
    boolean dragged = false;
    Thread test;

    public MainScene(Stage primaryStage)
    {
        super(new VoiceRecognitionPane(primaryStage), 200, 200, Color.TRANSPARENT);
        mainPane = (VoiceRecognitionPane)this.getRoot();
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
            	double size = 1;
                if(al > 500)
                {
                	int temp = al/5 + al/(100000/(al*2));
                	if(temp < 500)
                	{
                		temp = 500;
                	}
                	size = (double)temp / 500;
                    mainPane.startRecognize.setScale(size);
                }
            }
        }, GoogleSpeechApiHelper.API_KEY);
        mainPane.startRecognize.setRecognizeListener(new VoiceRecognitionButton.IRecognizeListener()
        {
            public void startRecognize()
            {
                voice.startRecognize();
            }
            public void stopRecognize()
            {
                voice.stopRecognize();
                mainPane.startRecognize.returnScale();
            }
        });
        /*
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
                    if(test.isAlive())
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
                        for(int i = 0; i < 100; i++)
                        {
                            try
                            {
                                Thread.sleep(75);
                            }
                            catch(Exception e)
                            {
                                System.out.println(e.getMessage());
                            }
                            final double size = ((double)j / 40) + (double)1;
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
                                j += (40 - j) / 9 + 1;
                            }
                            else
                            {
                                j -= (40 - j) / 9 + 1;
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
        */
    }
}