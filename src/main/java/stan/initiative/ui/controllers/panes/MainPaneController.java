package stan.initiative.ui.controllers.panes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class MainPaneController
    implements Initializable
{
    static public VBox getRoot()
    {
        try
        {
            return FXMLLoader.load(MainPaneController.class.getResource("/fxml/panes/MainPane.fxml"));
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage());
        }
        return null;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }
}