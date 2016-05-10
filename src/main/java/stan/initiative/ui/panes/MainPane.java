package stan.initiative.ui.panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import stan.initiative.res.values.Colors;
import stan.initiative.ui.controls.buttons.VoiceRecognitionButton;

public class MainPane
    extends VBox
{
    //VIEWS
    public Text audioLevel;
    //public StackPane stack;
    public VoiceRecognitionButton startRecognize;
    public Button stopRecognize;

	public MainPane()
	{
        initViews();
		init();
	}
    private void initViews()
    {
		//StackPane stack = new StackPane();
        startRecognize = new StanButton();
        startRecognize.setText("startRecognize");
        stopRecognize = new Button();
        stopRecognize.setText("stopRecognize");
        audioLevel = new Text(25, 25, "audioLevel");
        //audioLevel.setFill(Colors.red);
		//stack.getChildren().add(startRecognize);
		//stack.setHeight(50);
        this.getChildren().add(startRecognize);
        //this.getChildren().add(stack);
        this.getChildren().add(stopRecognize);
        this.getChildren().add(audioLevel);
    }
	private void init()
	{

	}
}