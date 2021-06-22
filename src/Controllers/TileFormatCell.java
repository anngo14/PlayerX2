package Controllers;

import Models.Tile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class TileFormatCell extends ListCell<Tile> {
    private FXMLLoader loader;
    private StackPane panel;
    private Tile tile;

    @FXML
    ImageView tileImage;

    @FXML
    Label tileLabel;

    @FXML
    HBox tileHbox;

    public TileFormatCell(StackPane panel){
        this.panel = panel;
    }

    @Override
    protected void updateItem(Tile t, boolean b) {
        tile = t;
        super.updateItem(t, b);
        setStyle("-fx-control-inner-background: rgba(0, 0, 0, 0);");
        if(t == null || b)
        {
            setText(null);
            setGraphic(null);
        }
        else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/Views/ListCellTileView.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            tileImage.setImage(new Image(String.valueOf(getClass().getResource(tile.getIcon()))));
            tileLabel.setText(tile.getTitle());

            setText(null);
            setGraphic(tileHbox);
        }
    }
}
