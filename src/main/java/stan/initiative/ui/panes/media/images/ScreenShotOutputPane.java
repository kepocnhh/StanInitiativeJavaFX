package stan.initiative.ui.panes.media.images;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import stan.initiative.listeners.ui.panes.media.images.IScreenShotOutputPaneListener;

public class ScreenShotOutputPane
    extends HBox
{
	private IScreenShotOutputPaneListener lis;
	
    //VIEWS
    private HBox output;
	
    public ScreenShotOutputPane(IScreenShotOutputPaneListener l)
    {
        super();
        this.setStyle("-fx-background-color: null");
		lis = l;
		Button f = new Button();
        f.setId("ss_tofolder_button");
        f.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
				lis.folder();
			}
		});
		Button x = new Button();
        x.setId("ss_exit_button");
        x.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
				lis.exit();
			}
		});
		Button t = new Button();
        t.setId("ss_totelegram_button");
        t.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
				lis.telegram();
			}
		});
		this.getChildren().addAll(f, x, t);
    }
}