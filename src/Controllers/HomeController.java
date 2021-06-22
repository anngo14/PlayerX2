package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import Models.Tile;
import Models.Users;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Controller, Initializable {
    private DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Users user;
    private List<Tile> tileList;

    @FXML
    StackPane panel;

    @FXML
    Label userLabel;

    @FXML
    Label timeLabel;

    @FXML
    ListView<Tile> topListView;

    public HomeController(){
        user = Helpers.getCurrentUser();
        tileList = user.getTileList();
    }

    @FXML
    public void settingsAction(){
        Helpers.switchViews(ViewType.SETTINGSVIEW, panel);
    }

    @FXML
    public void backAction(){
        Users user = Helpers.getCurrentUser();
        List<Tile> tileList = user.getTileList();
        tileList.remove(tileList.remove(tileList.size() - 1));
        Helpers.switchViews(ViewType.WELCOMEVIEW, panel);
    }

    public void updateLists(){
        topListView.getItems().clear();
        Tile addFolder = new Tile("Add Folder", "/Assets/sign-add-icon.png", "", ViewType.NEWFOLDERVIEW);
        tileList.add(addFolder);
        ObservableList<Tile> tiles$ = FXCollections.observableArrayList(tileList);
        topListView.setCellFactory(x -> new TileFormatCell(panel));
        topListView.setItems(tiles$);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(user.getName());

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                event -> timeLabel.setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        topListView.setOnKeyPressed(new TileEventHandler(topListView, panel));
        topListView.setOnMousePressed(new TileEventHandler(topListView, panel));
        updateLists();
    }
}
