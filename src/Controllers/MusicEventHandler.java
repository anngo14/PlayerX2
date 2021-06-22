package Controllers;

import Helpers.Helpers;
import Models.Media;
import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class MusicEventHandler implements EventHandler {
    private ListView<Media> node;
    private StackPane panel;

    public MusicEventHandler(Node n, StackPane panel){
        node = (ListView) n;
        this.panel = panel;
    }

    public void changeToMusicPlayer(Media media){
        FXMLLoader loader = new FXMLLoader(Helpers.class.getResource("/Views/MusicPlayerView.fxml"));
        loader.setController(new MusicPlayerController(media));
        try {
            StackPane pane = loader.load();
            panel.getChildren().setAll(pane);
            FadeTransition fade = new FadeTransition(Duration.millis(500), panel);
            fade.setFromValue(0);
            fade.setToValue(1);

            fade.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(Event event) {
        Media select = null;
        if(event.getEventType() == KeyEvent.KEY_PRESSED){
            if(((KeyEvent) event).getCode() == KeyCode.ENTER)
            {
                select = node.getSelectionModel().getSelectedItem();
                changeToMusicPlayer(select);
            }
        } else if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
            if (((MouseEvent) event).isPrimaryButtonDown() && ((MouseEvent) event).getClickCount() == 2) {
                select = node.getSelectionModel().getSelectedItem();
                changeToMusicPlayer(select);
            }
        }
    }
}
