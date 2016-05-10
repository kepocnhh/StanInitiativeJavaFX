package stan.initiative.ui.controls.buttons;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class VoiceRecognitionButton
    extends Button
{
    public interface IMouseEventButtonListener
    {
        void mousePressed(MouseEvent event);
        void mouseDragged(MouseEvent event);
    }
    public interface IRecognizeListener
    {
        void startRecognize();
        void stopRecognize();
    }

    private boolean dragged = false;
    private boolean startRecognize = false;
    private IMouseEventButtonListener mouseEventListener;
    private IRecognizeListener recognizeListener;

    public VoiceRecognitionButton()
    {
        super();
        setId("vr_button_stop");
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
    public void setRecognizeListener(IRecognizeListener l)
    {
    	recognizeListener = l;
        this.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(dragged)
                {
                    return;
                }
                if(startRecognize)
                {
                	recognizeListener.stopRecognize();
        			setId("vr_button_stop");
                }
                else
                {
                	recognizeListener.startRecognize();
        			setId("vr_button_start");
                }
                startRecognize = !startRecognize;
            }
        });
    }
    public void setScale(double scale)
    {
        this.setScaleX(scale);
        this.setScaleY(scale);
    }
    public void returnScale()
    {
        this.setScale(1);
    }
}