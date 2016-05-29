package stan.initiative.ui.scenes;

import java.io.File;
import java.util.ArrayList;
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

import stan.initiative.block.note.Block;
import stan.initiative.block.note.BNCore;
import stan.initiative.commander.Commander;
import stan.initiative.helpers.FileHelper;
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
    static private final int height = 224;
    static private final int width = 224;

    private VoiceRecognitionPane mainPane;
    private FileChooser fileChooser;
    private Stage primaryStage;

    private Voice voice;

    public MainScene(Stage pStage)
    {
        super(new VoiceRecognitionPane(pStage), height, width, Color.TRANSPARENT);
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
        MenuItem openBlockNote = new MenuItem("Open BlockNote");
        MenuItem exit = new MenuItem("Exit");
        openConfigure.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                openConfigureFile();
            }
        });
        openBlockNote.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                openBlockNote();
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
        contextMenu.getItems().addAll(openConfigure, openBlockNote, exit);
        return contextMenu;
    }
    private void initVoiceRecognition(String googleSpeechApiKey)
    {
        voice = new Voice(new Voice.IResponseListener()
        {
            @Override
            public void getSpeech(GoogleResponse deserialized)
            {
				//System.out.println("responseString - " + deserialized.responseString);
				//System.out.println("responseObject - " + deserialized.responseObject);
				ArrayList alternatives = getAlternatives((HashMap)deserialized.responseObject);
				if(alternatives.size() > 0)
				{
					parseAlternatives(alternatives);
				}
            }
            @Override
            public void audioLevel(int al)
            {
                double size = 1;
                if(al > 1500)
                {
                	double temp = 0;
	                if(al > 3500)
	                {
	                	temp = 3500;
	                }
	                else
	                {
                    	temp = al;
	                }
                	temp -= (temp-1500)/2;
					//System.out.println("temp - " + temp);
                	size = temp/1500;
					//System.out.println("size - " + size);
                }
                mainPane.startRecognize.setScale(size);
            }
        }, googleSpeechApiKey)
		{
			@Override
			public GoogleResponse deSerialize(String response)
			{
				response = response.replace("{\"result\":[]}","");
				if(response.length() == 0)
				{
					response = "{\"result\":[{\"alternative\":[]}]}";
				}
				JSONParser parser = new JSONParser();
				HashMap obj = null;
				try
				{
					obj = (HashMap) parser.parse(response);
				}
				catch(Exception e)
				{
					System.out.println("parse response error - " + e.getMessage());
				}
				return new GoogleResponse<HashMap>(obj, response);
			}
		};
    }
	private void parseAlternatives(ArrayList alternatives)
	{
		System.out.println("response - " + ((HashMap)alternatives.get(0)).get("transcript"));
	}
	
    private ArrayList getAlternatives(HashMap responseObject)
    {
    	return (ArrayList) ((HashMap)((ArrayList)responseObject.get("result")).get(0)).get("alternative");
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
    	String result = FileHelper.readFile(filename);
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
    	//initCommander((HashMap)main.get("commander"));
        initBlockNote((HashMap)main.get("blocknote"));
    	HashMap google = (HashMap)main.get("google");
    	HashMap speechapi = (HashMap)google.get("speechapi");
    	SpeechApiHelper.API_KEY = (String)speechapi.get("apikey");
    	initFromConfiguration();
    }
    private void initCommander(HashMap commander)
    {
		Commander.getInstance().initData((ArrayList)commander.get("modes"), (ArrayList)commander.get("states"), (ArrayList)commander.get("commands"));
		for(int i=0; i<Commander.getInstance().modes.length; i++)
		{
			System.out.println(i + ") " + Commander.getInstance().modes[i].name);
		}
    }
    private void initBlockNote(HashMap blocknote)
    {
        BNCore.getInstance().createBlockNote("E:/Downloads/StanInitiative/blocknote", "awesomebn");
	}

    private void openBlockNote()
    {
        File file = fileChooser.showOpenDialog(primaryStage);
        if(file != null && file.exists())
        {
            BNCore.getInstance().openBlockNote(file.getAbsolutePath());
            Block block = BNCore.getInstance().getActualBlock();
            if(block != null)
            {
                System.out.println("____BLOCK____");
                System.out.println("id - " + block.id);
                System.out.println("name - " + block.name);
                System.out.println("blocks:" + block.blocks.size());
            }
        }
        else
        {

        }
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