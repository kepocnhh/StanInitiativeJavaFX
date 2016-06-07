package stan.initiative.ui.scenes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import stan.initiative.commander.Commander;
import stan.initiative.commander.Controller;
import stan.initiative.commander.units.states.MusicPlayerState;
import stan.initiative.helpers.FileHelper;
import stan.initiative.helpers.google.SpeechApiHelper;
import stan.initiative.helpers.json.JSONParser;
import stan.initiative.listeners.voice.IRecognizeListener;
import stan.initiative.listeners.ui.panes.media.music.IMusicPlayerPaneListener;
import stan.initiative.media.music.Player;
import stan.initiative.media.images.ScreenShot;
import stan.initiative.res.values.Strings;
import stan.initiative.telegram.Telegram;
import stan.initiative.ui.controls.buttons.VoiceRecognitionButton;
import stan.initiative.ui.panes.VoiceRecognitionPane;
import stan.initiative.ui.panes.media.music.MusicPlayerPane;
import stan.initiative.ui.stages.MusicPlayerStage;

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
    private Stage musicPlayerStage;

    private Voice voice;

    public MainScene(Stage pStage)
    {
        super(new VoiceRecognitionPane(pStage), width, height, Color.TRANSPARENT);
        this.mainPane = (VoiceRecognitionPane)this.getRoot();
        this.fileChooser = new FileChooser();
        this.primaryStage = pStage;
        //initMusicPlayerScene();
        init();
    }
    private void initMusicPlayerScene()
    {
        this.musicPlayerStage = new Stage();
        this.musicPlayerStage.setAlwaysOnTop(true);
        this.musicPlayerStage.setScene(new Scene(new MusicPlayerPane(this.musicPlayerStage, new IMusicPlayerPaneListener()
        {
            public void exit()
            {
                musicPlayerStage.hide();
            }
        }), 48 * 3, 48 + 48 / 2, Color.TRANSPARENT));
        this.musicPlayerStage.initStyle(StageStyle.TRANSPARENT);
        this.musicPlayerStage.getScene().getStylesheets().add("css/StanTheme.css");
    }
    private void init()
    {
        mainPane.startRecognize.setContextMenu(initContextMenu());
    }
    private ContextMenu initContextMenu()
    {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem openConfigure = new MenuItem("Open configuration file");
        MenuItem music = new MenuItem("Music player");
        MenuItem exit = new MenuItem("Exit");
        openConfigure.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                openConfigureFile();
            }
        });
        music.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(Controller.extraStates.get(MusicPlayerState.ID_KEY) != null)
                {
                    System.out.println("MainScene Controller extraStates MusicPlayerState != null");
                    Commander.getInstance().addNewState(Controller.extraStates.get(MusicPlayerState.ID_KEY));
                }
                //MusicPlayerStage.getInstance().showMusicPlayer();
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
        contextMenu.getItems().addAll(openConfigure, music, exit);
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
                    temp -= (temp - 1500) / 2;
                    //System.out.println("temp - " + temp);
                    size = temp / 1500;
                    //System.out.println("size - " + size);
                }
                mainPane.startRecognize.setScale(size);
            }
        }, googleSpeechApiKey)
        {
            @Override
            public GoogleResponse deSerialize(String response)
            {
                response = response.replace("{\"result\":[]}", "");
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
        String transcript = (String)((HashMap)alternatives.get(0)).get("transcript");
        System.out.println("response - " + transcript);
        parseTranscript(transcript);
    }
    private void parseTranscript(String transcript)
    {
        Commander.getInstance().parseKey(transcript.toLowerCase());
    }

    private ArrayList getAlternatives(HashMap responseObject)
    {
        return (ArrayList)((HashMap)((ArrayList)responseObject.get("result")).get(0)).get("alternative");
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
        HashMap obj = null;
        try
        {
            obj = (HashMap)new JSONParser().parse(result);
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
        initTelegram((HashMap)main.get("telegram"));
        initCommander((HashMap)main.get("commander"));
        initMedia((HashMap)main.get("media"));
        HashMap google = (HashMap)main.get("google");
        HashMap speechapi = (HashMap)google.get("speechapi");
        SpeechApiHelper.API_KEY = (String)speechapi.get("apikey");
        initFromConfiguration();
    }
    private void initTelegram(HashMap telegram)
    {
        HashMap bot = (HashMap)telegram.get("bot");
        Telegram.getInstance().setBot(
            ((Long)bot.get("id")).intValue(),
            (String)bot.get("token"),
            ((Long)bot.get("chatIdMe")).intValue());
    }
    private void initCommander(HashMap commander)
    {
        Commander.getInstance().addExtra(new Controller());
        Commander.getInstance().initData((ArrayList)commander.get("modes"), (ArrayList)commander.get("states"), (ArrayList)commander.get("commands"));
    }
    private void initMedia(HashMap media)
    {
        initMusic((HashMap)media.get("music"));
        initImages((HashMap)media.get("images"));
    }
    private void initMusic(HashMap music)
    {
        MusicPlayerStage.getInstance();
        Player.getInstance()
        .setDefault()
        .setVolume(0.2)
        .randomOn()
        .fromFolder((String)music.get("mainFolder"));
    }
    private void initImages(HashMap images)
    {
        ScreenShot.getInstance().setPath((String)images.get("screenShotFolder"));
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
        String answer = Telegram.getInstance()
        	.getBot()
        	.sendPhotoMe(ScreenShot.getInstance().grabScreenBytes(15, 15, 111, 111));
        System.out.println("Telegram - " + answer);
        return false;
    }
    //@Override
    public boolean startRecognize1()
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