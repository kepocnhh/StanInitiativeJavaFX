package stan.initiative.ui.panes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class BNPane
    extends HBox
{
    //VIEWS
    Button back = new Button("<");
    Text blockName = new Text();
    ListView<String> listView = new ListView<String>();

	public BNPane()
	{
        super();
        this.setStyle("-fx-background-color: null");
        this.getChildren().add(initLeftPane());
	}

    private VBox initLeftPane()
    {
        ObservableList<String> items = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App");
        listView.setItems(items);
        //listView.setMaxHeight(Control.USE_PREF_SIZE);
        VBox blockInfo = new VBox();
        blockInfo.setStyle("-fx-background-color: red");
        //blockInfo.setPrefSize(22,22);
        blockInfo.setPrefWidth(22);
        blockInfo.setMinWidth(0);
        //blockInfo.setMinHeight(0);
        //blockInfo.setMaxHeight(22);
        blockInfo.setMaxWidth(22);
        HBox leftTopPane = initLeftTopPane();
        leftTopPane.setMaxWidth(Double.MAX_VALUE);
        blockInfo.getChildren().addAll(leftTopPane, listView);
        //blockInfo.getChildren().add(listView);
        return blockInfo;
    }
    private HBox initLeftTopPane()
    {
        HBox blockTop = new HBox();
        blockTop.setStyle("-fx-background-color: green");
        blockName.setText("block nameaaaaaaaaaaaaaaaaaaaaaa!!!!---+");
        blockTop.setMaxWidth(22);
        blockTop.getChildren().addAll(back, blockName);
        return blockTop;
    }
}