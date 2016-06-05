package stan.initiative.ui.controls.buttons.media.music;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import stan.initiative.listeners.ui.controls.buttons.IMouseEventButtonListener;
import stan.initiative.listeners.ui.controls.buttons.media.music.IExitButtonListener;

public class MusicPlayerExitButton
    extends Button
{
    private boolean dragged = false;
    private IMouseEventButtonListener mouseEventListener;
    private IExitButtonListener exitListener;

    public MusicPlayerExitButton(IExitButtonListener e)
    {
        super();
        setPlayStyle();
        exitListener = e;
        this.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(dragged)
                {
                    return;
                }
                exitListener.exit();
            }
        });
    }
    public void setMouseEventListener(IMouseEventButtonListener l)
    {
        mouseEventListener = l;
        this.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                dragged = false;
                mouseEventListener.mousePressed(event);
            }
        });
        this.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                dragged = true;
                mouseEventListener.mouseDragged(event);
            }
        });
    }

    public void setPlayStyle()
    {
        setId("mp_exit_button");
    }
}