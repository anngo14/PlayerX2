package Controllers;

import Models.Media;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class MediaFormatCell extends ListCell<Media> {
    private FXMLLoader loader;
    private Media media;

    @FXML
    Label mediaNameLabel;

    @FXML
    ImageView mediaImage;

    @FXML
    HBox mediaHbox;

    public MediaFormatCell(){
    }

    @Override
    protected void updateItem(Media m, boolean b) {
        media = m;
        super.updateItem(m, b);
        if(m == null || b)
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
            mediaNameLabel.setText(media.getName());
            mediaImage.setImage(new Image(String.valueOf(getClass().getResource(media.getIcon()))));

            setText(null);
            setGraphic(mediaHbox);
        }
    }
}
