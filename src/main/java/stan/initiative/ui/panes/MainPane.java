package stan.initiative.ui.panes;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import stan.initiative.res.values.Colors;

public class MainPane
    extends VBox
{
    //VIEWS
    public Text audioLevel;
    public Button startRecognize;
    public Button stopRecognize;

	public MainPane()
	{
        initViews();
        init();
	}
    private void initViews()
    {
        startRecognize = new Button();
        startRecognize.setText("startRecognize");
        stopRecognize = new Button();
        stopRecognize.setText("stopRecognize");
        audioLevel = new Text(25, 25, "audioLevel");
        audioLevel.setFill(Colors.red);
        this.getChildren().add(startRecognize);
        this.getChildren().add(stopRecognize);
        this.getChildren().add(audioLevel);
    }
	private void init()
	{

	}
}