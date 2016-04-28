package stan.initiative;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import stan.initiative.res.values.Strings;
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
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new MainScene(primaryStage));
        primaryStage.show();
    }
}