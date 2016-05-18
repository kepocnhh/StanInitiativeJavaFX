package stan.initiative.ui.scenes;

import java.io.File;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import stan.initiative.helpers.FileReaderHelper;
import stan.initiative.helpers.google.SpeechApiHelper;
import stan.initiative.helpers.json.JSONParser;
import stan.initiative.listeners.voice.IRecognizeListener;
import stan.initiative.res.values.Strings;
import stan.initiative.ui.controls.buttons.VoiceRecognitionButton;
import stan.initiative.ui.panes.VoiceRecognitionPane;

import stan.voice.recognition.Voice;
import stan.voice.recognition.GoogleResponse;

public class MainScene
    extends Scene
    implements IRecognizeListener
{
    private VoiceRecognitionPane mainPane;
    private FileChooser fileChooser;
    private Stage primaryStage;

    private Voice voice;
    boolean dragged = false;
    Thread test;

    public MainScene(Stage pStage)
    {
        super(new VoiceRecognitionPane(pStage), 200, 200, Color.TRANSPARENT);
        this.mainPane = (VoiceRecognitionPane)this.getRoot();
        this.fileChooser = new FileChooser();
        this.primaryStage = pStage;
        init();
    }
    private void init()
    {
        mainPane.startRecognize.setContextMenu(initContextMenu());
    }
    private ContextMenu initContextMenu()
    {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem openConfigure = new MenuItem("Open configuration file");
        MenuItem exit = new MenuItem("Exit");
        openConfigure.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                openConfigureFile();
            }
        });
        exit.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                exit();
            }
        });
        contextMenu.getItems().addAll(openConfigure, exit);
        return contextMenu;
    }
    private void initVoiceRecognition(String googleSpeechApiKey)
    {
        voice = new Voice(new Voice.IResponseListener()
        {
            @Override
            public void getSpeech(GoogleResponse deserialized)
            {
        		//System.out.println(deserialized.toString());
            }
            @Override
            public void audioLevel(int al)
            {
                double size = 1;
                if(al > 500)
                {
                    int temp = al / 5 + al / (100000 / (al * 2));
                    if(temp < 500)
                    {
                        temp = 500;
                    }
                    size = (double)temp / 500;
                    mainPane.startRecognize.setScale(size);
                }
            }
        }, googleSpeechApiKey)
		{
			@Override
			public GoogleResponse deSerialize(String response)
			{
				JSONParser parser = new JSONParser();
				HashMap obj = null;
				try
				{
					obj = (HashMap)parser.parse(response);
				}
				catch(Exception e)
				{
					System.out.println("parse response error - " + e.getMessage());
					return null;
				}
				return new GoogleResponse<HashMap>(obj, response);
			}
		};
    }
    private void initFromConfiguration()
    {
        initVoiceRecognition(SpeechApiHelper.API_KEY);
        mainPane.startRecognize.setRecognizeListener(this);
    }
    private void openConfigureFile()
    {
        File file = fileChooser.showOpenDialog(primaryStage);
        if(file != null && file.exists())
        {
            initFromFile(file.getAbsolutePath());
        }
        else
        {

        }
    }
    private void initFromFile(String filename)
    {
    	String result = FileReaderHelper.readFile(filename);
    	JSONParser parser = new JSONParser();
    	HashMap obj = null;
    	try
    	{
    		obj = (HashMap)parser.parse(result);
    	}
    	catch(Exception e)
    	{
        	System.out.println("Read file error - " + e.getMessage());
        	return;
    	}
    	initFromHashMap(obj);
    }
    private void initFromHashMap(HashMap main)
    {
    	HashMap telegram = (HashMap)main.get("telegram");
    	HashMap google = (HashMap)main.get("google");
    	HashMap speechapi = (HashMap)google.get("speechapi");
    	SpeechApiHelper.API_KEY = (String)speechapi.get("apikey");
    	initFromConfiguration();
    }
    private void exit()
    {
        if(voice != null)
        {
            voice.stopRecognize();
        }
        Platform.exit();
        System.exit(0);
    }

    @Override
    public boolean startRecognize()
    {
		try
		{
			voice.startRecognize();
		}
		catch(Exception e)
		{
			System.out.println("Voice start Recognize error - " + e.getMessage());
			return false;
		}
        return true;
    }
    @Override
    public void stopRecognize()
    {
        voice.stopRecognize();
        mainPane.startRecognize.returnScale();
    }
}