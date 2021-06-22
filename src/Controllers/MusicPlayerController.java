package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class MusicPlayerController implements Initializable, Controller{

    private static DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private MediaPlayer player;
    private Media media;
    private Models.Media selectedMusic;
    private List<Models.Media> musicList;

	@FXML
	StackPane panel;

	@FXML
	Label timeLabel;

	@FXML
	Label songLabel;

	@FXML
	ImageView playImg;

	@FXML
	ImageView pauseImg;

	@FXML
	ImageView reverseImg;

	@FXML
	ImageView forwardImg;

	@FXML
	Button playButton;

	@FXML
	Button pauseButton;

	@FXML
	Button reverseButton;

	@FXML
	Button forwardButton;

	public MusicPlayerController(Models.Media music)
	{
		selectedMusic = music;
		musicList = MusicFolderController.getFiles();
		media = new Media(new File(selectedMusic.getPath()).toURI().toString());
		player = new MediaPlayer(media);
		player.setAutoPlay(true);
		player.setOnEndOfMedia(() -> fastForwardTrack());
	}

	@FXML
	public void backToHome()
	{
		player.stop();
		Helpers.switchViews(ViewType.HOMEVIEW, panel);
	}
	@FXML
	public void backToList()
	{
		player.stop();
		Helpers.switchToTilesView(ViewType.MUSICVIEW, panel, MusicFolderController.getTile());
	}
	@FXML
	public void playSong()
	{
		player.play();
	}
	@FXML
	public void reverseTrack()
	{
		player.stop();
		int index = getIndex(selectedMusic, musicList);
		if(index == 0)
		{
			media = new Media(new File(musicList.get(musicList.size()-1).getPath()).toURI().toString());
			songLabel.setText(musicList.get(musicList.size()-1).getName());
			selectedMusic = musicList.get(musicList.size()-1);
		}
		else
		{
			media = new Media(new File(musicList.get(index-1).getPath()).toURI().toString());
			songLabel.setText(musicList.get(index-1).getName());
			selectedMusic = musicList.get(index-1);
		}
		player = new MediaPlayer(media);
		player.setAutoPlay(true);
		player.setOnEndOfMedia(() -> fastForwardTrack());
	}
	@FXML
	public void pauseTrack()
	{
		player.pause();
	}
	@FXML
	public void fastForwardTrack()
	{
		player.stop();
		int index = getIndex(selectedMusic, musicList);
		if(index == musicList.size()-1)
		{
			media = new Media(new File(musicList.get(0).getPath()).toURI().toString());
			songLabel.setText(musicList.get(0).getName());
			selectedMusic = musicList.get(0);
		}
		else
		{
			media = new Media(new File(musicList.get(index+1).getPath()).toURI().toString());
			songLabel.setText(musicList.get(index+1).getName());
			selectedMusic = musicList.get(index+1);

		}
		player = new MediaPlayer(media);
		player.setAutoPlay(true);
		player.setOnEndOfMedia(() -> fastForwardTrack());
	}
	public int getIndex(Models.Media m, List<Models.Media> list)
	{
		int index = -1;
		for(int i = 0; i < list.size(); i++)
		{
			if(m.getPath().equals(list.get(i).getPath()))
			{
				index = i;
			}
		}
		return index;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		songLabel.setText(selectedMusic.getName());
		pauseButton.focusedProperty().addListener(new HoverFadeListener(pauseImg));
		reverseButton.focusedProperty().addListener(new HoverFadeListener(reverseImg));
		forwardButton.focusedProperty().addListener(new HoverFadeListener(forwardImg));
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
				event -> timeLabel.setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
				new KeyFrame(Duration.seconds(1)));

		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
}