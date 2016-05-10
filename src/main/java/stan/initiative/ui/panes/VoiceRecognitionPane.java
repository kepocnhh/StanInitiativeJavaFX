package stan.initiative.ui.panes;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import stan.initiative.res.values.Colors;
import stan.initiative.ui.controls.buttons.VoiceRecognitionButton;

public class VoiceRecognitionPane
    extends HBox
{
    private double xOffset = 0;
    private double yOffset = 0;

    //VIEWS
    public VoiceRecognitionButton startRecognize;

	public VoiceRecognitionPane(final Stage primaryStage)
	{
        super();
        //this.setId("circle_pane");
        this.setStyle("-fx-background-color: null");
        startRecognize = new VoiceRecognitionButton();
        startRecognize.setMouseEventListener(new VoiceRecognitionButton.IMouseEventButtonListener()
        {
            public void mousePressed(MouseEvent event)
            {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
            public void mouseDragged(MouseEvent event)
            {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(startRecognize);
	}
}