package stan.initiative.ui.scenes;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import stan.initiative.res.values.Strings;
import stan.initiative.ui.panes.MainPane;

public class MainScene
    extends Scene
{
    private double xOffset = 0;
    private double yOffset = 0;

	public MainScene(final Stage primaryStage)
	{
		super(new MainPane(), 100, 100);
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
		init();
	}
	private void init()
	{

	}
}