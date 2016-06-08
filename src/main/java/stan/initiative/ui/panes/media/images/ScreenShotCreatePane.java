package stan.initiative.ui.panes.media.images;

import java.awt.image.BufferedImage;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import stan.initiative.listeners.ui.panes.media.images.IScreenShotOutputPaneListener;
import stan.initiative.listeners.ui.panes.media.images.IScreenShotPaneListener;

public class ScreenShotCreatePane
    extends Pane
{
	private BufferedImage image;
    private Rectangle rect;
	private double centerX;
	private double centerY;
	private IScreenShotPaneListener listener;
	
    //VIEWS
    private Pane parent;
    private ScreenShotOutputPane output;
	
    public ScreenShotCreatePane(IScreenShotPaneListener l)
    {
        super();
        this.setStyle("-fx-background-color: null");
		listener = l;
		//
		output = new ScreenShotOutputPane(new IScreenShotOutputPaneListener()
		{
			public void folder()
			{
				listener.folder(image);
			}
			public void telegram()
			{
				listener.telegram(image);
			}
            public void exit()
            {
				listener.exit();
            }
		});
		//
		parent = new Pane();
        parent.setStyle("-fx-background-color: rgba(0,0,0,0.5)");
		parent.prefWidthProperty().bind(this.widthProperty());
		parent.prefHeightProperty().bind(this.heightProperty());
        rect = new Rectangle();
        rect.setFill(Color.web("white", 0.5));
		parent.getChildren().add(rect);
		//
		this.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				if(event.getButton() == MouseButton.PRIMARY)
				{
					parent.setVisible(false);
					output.setVisible(true);
					output.setLayoutX(event.getX() - output.getWidth()/2);
					output.setLayoutY(event.getY());
					image = listener.grabScreen((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
				}
				else if(event.getButton() == MouseButton.SECONDARY)
				{
					listener.exit();
				}
			}
		});
        this.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
				if(event.getButton() == MouseButton.PRIMARY)
				{
					rect.setWidth(0);
					rect.setHeight(0);
					centerX = event.getX();
					centerY = event.getY();
					rect.setX(event.getX());
					rect.setY(event.getY());
				}
            }
        });
        this.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
				if(event.getButton() == MouseButton.PRIMARY)
				{
					double x = event.getX() - centerX;
					if(x<0)
					{
						rect.setX(event.getX());
						rect.setWidth(centerX - event.getX());
					}
					else
					{
						rect.setX(centerX);
						rect.setWidth(x);
					}
					double y = event.getY() - centerY;
					if(y<0)
					{
						rect.setY(event.getY());
						rect.setHeight(centerY - event.getY());
					}
					else
					{
						rect.setY(centerY);
						rect.setHeight(y);
					}
				}
            }
        });
		this.getChildren().addAll(parent, output);
    }
	
	public void showScreenShot(double width, double height)
	{
        this.rect.setX(0);
        this.rect.setY(0);
		this.rect.setWidth(width);
		this.rect.setHeight(height);
		this.parent.setVisible(true);
		this.output.setVisible(false);
	}
}