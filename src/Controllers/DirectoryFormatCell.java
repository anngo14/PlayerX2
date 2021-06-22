package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class DirectoryFormatCell extends ListCell<String> {
    private FXMLLoader loader;

    @FXML
    Label mediaNameLabel;

    @FXML
    ImageView mediaImage;

    @FXML
    HBox mediaHbox;

    public DirectoryFormatCell(){

    }

    @Override
    protected void updateItem(String s, boolean b) {
        super.updateItem(s, b);
        if(s == null || b)
        {
            setText(null);
            setGraphic(null);
        }
        else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/Views/ListCellMediaView.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mediaNameLabel.setText(s);
            mediaImage.setImage(new Image(String.valueOf(getClass().getResource("/Assets/folder-icon.png"))));

            setText(null);
            setGraphic(mediaHbox);
        }
    }
}
