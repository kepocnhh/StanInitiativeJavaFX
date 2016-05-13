package stan.initiative.ui.scenes;

import java.io.File;

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
import stan.initiative.listeners.voice.IRecognizeListener;
import stan.initiative.res.values.Strings;
import stan.initiative.ui.controls.buttons.VoiceRecognitionButton;
import stan.initiative.ui.panes.VoiceRecognitionPane;

import stan.voice.recognition.Voice;
import stan.voice.recognition.google.response.GoogleResponse;

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
        }, googleSpeechApiKey);
    }
    private void initFromConfiguration(String googleSpeechApiKey)
    {
        initVoiceRecognition(googleSpeechApiKey);
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
        System.out.println(result);
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
        voice.startRecognize();
        return true;
    }
    @Override
    public void stopRecognize()
    {
        voice.stopRecognize();
        mainPane.startRecognize.returnScale();
    }
}