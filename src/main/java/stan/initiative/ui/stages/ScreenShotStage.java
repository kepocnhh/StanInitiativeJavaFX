package stan.initiative.ui.stages;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import stan.initiative.listeners.ui.panes.media.images.IScreenShotPaneListener;
import stan.initiative.media.images.ScreenShot;
import stan.initiative.telegram.IRequestListener;
import stan.initiative.telegram.Telegram;
import stan.initiative.ui.panes.media.images.ScreenShotCreatePane;

public class ScreenShotStage
    extends Stage
{
    static private ScreenShotStage instance;
    static public ScreenShotStage getInstance()
    {
        if(instance == null)
        {
            instance = new ScreenShotStage();
        }
        return instance;
    }

    private Rectangle2D screen;
	
    //VIEWS
	private ScreenShotCreatePane sPane;
	
    private ScreenShotStage()
    {
        super();
        this.setAlwaysOnTop(true);
        screen = Screen.getPrimary().getVisualBounds();
		this.sPane = new ScreenShotCreatePane(new IScreenShotPaneListener()
        {
			public void folder(BufferedImage image)
			{
				ScreenShot.getInstance().saveScreen(image);
				ScreenShotStage.getInstance().hideScreenShot();
			}
			public void telegram(BufferedImage image)
			{
				Telegram.getInstance()
					.getBot()
					.sendPhotoMe(image, new IRequestListener()
					{
						public void answer(String answer)
						{
							System.out.println("Telegram answer - " + answer);
						}
						public void error(IOException error)
						{
							System.out.println("Telegram error - " + error.getMessage());
						}
					});
				ScreenShotStage.getInstance().hideScreenShot();
			}
            public BufferedImage grabScreen(int x, int y, int w, int h)
            {
                hide();
				BufferedImage image = ScreenShot.getInstance().grabScreen(x, y, w, h);
                show();
				return image;
            }
            public void exit()
            {
				ScreenShotStage.getInstance().hideScreenShot();
            }
        });
        this.setScene(new Scene(sPane, screen.getWidth(), screen.getHeight(), Color.TRANSPARENT));
        this.initStyle(StageStyle.TRANSPARENT);
        this.getScene().getStylesheets().add("css/StanTheme.css");
    }
    public void hideScreenShot()
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                if(ScreenShotStage.getInstance().isShowing())
                {
                    ScreenShotStage.getInstance().hide();
                }
            }
        });
    }
    public void showScreenShot()
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                if(ScreenShotStage.getInstance().isShowing())
                {
                    return;
                }
				sPane.showScreenShot(screen.getWidth(), screen.getHeight());
                ScreenShotStage.getInstance().show();
            }
        });
    }
}