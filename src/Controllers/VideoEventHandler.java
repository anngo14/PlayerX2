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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class VideoEventHandler implements EventHandler {
    private ListView<Media> node;
    private StackPane panel;
    private List<Media> videoList;

    public VideoEventHandler(Node n, StackPane panel){
        this.videoList = DirectoryChangeListener.getVideoList();
        node = (ListView) n;
        this.panel = panel;
    }

    public void changeToVideoPlayer(Media media){
        FXMLLoader loader = new FXMLLoader(Helpers.class.getResource("/Views/VideoPlayerView.fxml"));
        loader.setController(new VideoPlayerController(media));
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
                if(select.getName().matches(".*\\.mp4$")){
                    changeToVideoPlayer(select);
                } else{
                    try {
                        Desktop.getDesktop().open(new File(select.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
            if (((MouseEvent) event).isPrimaryButtonDown() && ((MouseEvent) event).getClickCount() == 2) {
                select = node.getSelectionModel().getSelectedItem();
                if(select.getName().matches(".*\\.mp4$")){
                    changeToVideoPlayer(select);
                } else{
                    try {
                        Desktop.getDesktop().open(new File(select.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
