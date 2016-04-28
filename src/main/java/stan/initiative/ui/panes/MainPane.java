package stan.initiative.ui.panes;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import javafx.scene.layout.StackPane;

public class MainPane
    extends StackPane
{
	public MainPane()
	{
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                System.out.println("Hello World!");
            }
        });
        this.getChildren().add(btn);
        init();
	}
	private void init()
	{

	}
}