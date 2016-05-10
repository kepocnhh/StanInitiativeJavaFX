package stan.initiative.ui.panes;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import stan.initiative.res.values.Colors;
import stan.initiative.ui.controls.buttons.StanButton;

public class MainPane
    extends VBox
    // extends Parent
{
    static private MainPane instance;
    static public MainPane getInstance()
    {
        if(instance == null)
        {
            try
            {
                instance = FXMLLoader.load(MainPane.class.getResource("/fxml/panes/MainPane.fxml"));
            }
            catch(Exception e)
            {
                System.out.print(e.getMessage());
            }
        }
        return instance;
    }

    //VIEWS
    public Text audioLevel;
    public StanButton startRecognize;
    public Button stopRecognize;

	public MainPane()
	{
        // initViews();
        // init();
	}
    private void initViews()
    {
        startRecognize = new StanButton();
        startRecognize.setText("startRecognize");
        stopRecognize = new Button();
        stopRecognize.setText("stopRecognize");
        audioLevel = new Text(25, 25, "audioLevel");
        //audioLevel.setFill(Colors.red);
        this.getChildren().add(startRecognize);
        this.getChildren().add(stopRecognize);
        this.getChildren().add(audioLevel);
    }
	private void init()
	{

	}
}