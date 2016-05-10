package stan.initiative;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;

import stan.initiative.ui.scenes.MainScene;

public class Main
    extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        //primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setAlwaysOnTop(true);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new MainScene(primaryStage));
        primaryStage.getScene().getStylesheets().add("css/StanTheme.css");
        primaryStage.show();
        primaryStage.toFront();
    }
}