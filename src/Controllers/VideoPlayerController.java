package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class VideoPlayerController implements Initializable, Controller{

    private static DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Models.Media selected;
    private static MediaPlayer player;
    private Media media;
    private List<Models.Media> videoList;

    @FXML
    MediaView viewer;

    @FXML
    Rectangle timeBox;

    @FXML
    Label timeLabel;

    @FXML
    Button listButton;

    @FXML
    Button hideButton;

    @FXML
    Button playButton;

    @FXML
    Button reverseButton;

    @FXML
    Button forwardButton;

    @FXML
    ImageView playImg;

    @FXML
    ImageView reverseImg;

    @FXML
    ImageView forwardImg;

    @FXML
    Label fileNameLabel;

    @FXML
    Slider slider;

    @FXML
    StackPane panel;

    @FXML
    StackPane mediaBar;

    public VideoPlayerController(Models.Media m)
    {
        selected = m;
        media = new Media(new File(selected.getPath()).toURI().toString());
        player = new MediaPlayer(media);
        player.setAutoPlay(true);
        videoList = DirectoryChangeListener.getVideoList();
        if(videoList.size() == 0){
            videoList = VideoFolderController.getFiles();
        }
    }

    @FXML
    public void playMedia()
    {
        if(playButton.getText().equals("||"))
        {
            player.pause();
            playButton.setText(">");
            playImg.setImage(new Image("/Assets/playicon.png"));
        }
        else
        {
            player.play();
            playButton.setText("||");
            playImg.setImage(new Image("/Assets/pauseicon.png"));
        }
    }

    @FXML
    public void reverseMedia()
    {
        if(player == null) return;
        player.pause();
        Double seekValue = player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100 - 2;
        if(seekValue <= 0)
        {
            return;
        }
        slider.setValue(seekValue);
        player.seek(player.getMedia().getDuration().multiply(slider.getValue() / 100));
        player.play();
    }

    @FXML
    public void forwardMedia()
    {
        if(player == null) return;
        player.pause();
        Double seekValue = player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100 + 2;
        if(seekValue >= player.getTotalDuration().toMillis() * 100)
        {
            return;
        }
        slider.setValue(seekValue);
        player.seek(player.getMedia().getDuration().multiply(slider.getValue() / 100));
        player.play();
    }

    @FXML
    public void backToList()
    {
        panel.setOnKeyPressed(null);
        panel.setCursor(Cursor.DEFAULT);
        player.stop();
        player.dispose();
        Helpers.switchToTilesView(ViewType.VIDEOVIEW, panel, VideoFolderController.getTile());
    }

    @FXML
    public void hideMediaBar()
    {
        if(mediaBar.getOpacity() == 1)
        {
            mediaBar.setOpacity(0);
            timeLabel.setOpacity(0);
            timeBox.setOpacity(0);
        }
        else
        {
            mediaBar.setOpacity(1);
            timeLabel.setOpacity(1);
            timeBox.setOpacity(1);
        }
    }

    protected void updatesValues()
    {
        Platform.runLater(new Runnable() {
            public void run()
            {
                slider.setValue(player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100);
            }
        });
    }

    public static MediaPlayer getPlayer() { return player; }

    public void playNext(){
        if(player != null){
            player.stop();
            player.dispose();
        }
        int index = getIndex(selected, videoList);

        if(index + 1 == videoList.size())
        {
            backToList();
        } else{
            Models.Media nextVideo = videoList.get(index + 1);
            selected = nextVideo;
            fileNameLabel.setText(selected.getName());
            if(!selected.getName().matches(".*\\.mp4$")) playNext();
            media = new Media(new File(nextVideo.getPath()).toURI().toString());
            player = new MediaPlayer(media);
            player.setAutoPlay(true);
            player.setOnEndOfMedia(() -> playNext());
            player.currentTimeProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable arg0) {
                    updatesValues();
                }
            });
            slider.setValue(0);
            slider.valueProperty().addListener(new VideoPlayerInvalidationListener(slider));
            slider.valueProperty().addListener(new SliderChangeListener(slider));
            panel.setOnKeyPressed(new MediaBarKeyEventHandler(mediaBar, timeLabel, timeBox, panel, playImg, playButton, slider));
            viewer.setMediaPlayer(player);
        }
    }

    public int getIndex(Models.Media media, List<Models.Media> list){
        int index = -1;
        for(int i = 0; i < list.size(); i++)
        {
            if(media.getPath().equals(list.get(i).getPath()))
            {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        hideMediaBar();
        fileNameLabel.setText(selected.getName());
        panel.setCursor(Cursor.NONE);
        viewer.setMediaPlayer(player);
        viewer.fitHeightProperty().bind(panel.heightProperty());
        viewer.fitWidthProperty().bind(panel.widthProperty());

        slider.valueProperty().addListener(new VideoPlayerInvalidationListener(slider));
        slider.valueProperty().addListener(new SliderChangeListener(slider));

        playButton.focusedProperty().addListener(new HoverFadeListener(playImg));
        reverseButton.focusedProperty().addListener(new HoverFadeListener(reverseImg));
        forwardButton.focusedProperty().addListener(new HoverFadeListener(forwardImg));

        panel.setOnKeyPressed(new MediaBarKeyEventHandler(mediaBar, timeLabel, timeBox, panel, playImg, playButton, slider));

        player.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                updatesValues();
            }
        });
        player.setOnEndOfMedia(() -> {
            if(VideoFolderController.getAutoPlay()){
                playNext();
            } else{
                backToList();
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                event -> timeLabel.setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}

class VideoPlayerInvalidationListener implements InvalidationListener{

    private Slider slider;
    private MediaPlayer player;
    public VideoPlayerInvalidationListener(Slider s) {
        slider = s;
        player = VideoPlayerController.getPlayer();
    }

    @Override
    public void invalidated(Observable arg0) {
        if(slider.isPressed()) {
            player.seek(player.getMedia().getDuration().multiply(slider.getValue() / 100));
        }
    }
}

class SliderChangeListener implements ChangeListener<Number> {

    private Slider slider;
    private MediaPlayer player;

    public SliderChangeListener(Slider s) {
        slider = s;
        player = VideoPlayerController.getPlayer();
    }
    @Override
    public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
        if(Math.abs(arg2.intValue() -  arg1.intValue()) == slider.getBlockIncrement())
        {
            slider.setValue(arg2.doubleValue());
            player.seek(player.getMedia().getDuration().multiply(slider.getValue() / 100));
        }
    }
}

class MediaBarKeyEventHandler implements EventHandler<KeyEvent> {
    private StackPane mediaBar;
    private Label timeLabel;
    private Rectangle timeBox;
    private StackPane panel;
    private MediaPlayer player;
    private ImageView playImg;
    private Button playButton;
    private Slider slider;

    public MediaBarKeyEventHandler(StackPane m, Label l, Rectangle r, StackPane s, ImageView play, Button playBtn, Slider slider)
    {
        mediaBar = m;
        timeLabel = l;
        timeBox = r;
        panel = s;
        player = VideoPlayerController.getPlayer();
        playButton = playBtn;
        playImg = play;
        this.slider = slider;
    }

    @Override
    public void handle(KeyEvent event) {
        if(event.getCode() == KeyCode.M)
        {
            if(mediaBar.getOpacity() == 1)
            {
                mediaBar.setOpacity(0);
                timeLabel.setOpacity(0);
                timeBox.setOpacity(0);
                panel.setCursor(Cursor.NONE);
            }
            else
            {
                mediaBar.setOpacity(1);
                timeLabel.setOpacity(1);
                timeBox.setOpacity(1);
                panel.setCursor(Cursor.DEFAULT);
            }
        } else if(event.getCode() == KeyCode.P && player != null){
            if(playButton.getText().equals("||"))
            {
                player.pause();
                playButton.setText(">");
                playImg.setImage(new Image("/Assets/playicon.png"));
            }
            else
            {
                player.play();
                playButton.setText("||");
                playImg.setImage(new Image("/Assets/pauseicon.png"));
            }
        } else if(event.getCode() == KeyCode.COMMA && player != null){
            player.pause();
            Double seekValue = player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100 - 2;
            if(seekValue <= 0)
            {
                return;
            }
            slider.setValue(seekValue);
            player.seek(player.getMedia().getDuration().multiply(slider.getValue() / 100));
            player.play();
        } else if(event.getCode() == KeyCode.PERIOD && player != null){
            player.pause();
            Double seekValue = player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100 + 2;
            if(seekValue >= player.getTotalDuration().toMillis() * 100)
            {
                return;
            }
            slider.setValue(seekValue);
            player.seek(player.getMedia().getDuration().multiply(slider.getValue() / 100));
            player.play();
        }
    }
}
