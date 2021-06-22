package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import Models.Media;
import Models.Tile;
import Models.Users;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class VideoFolderController implements Controller, Initializable {
    private static DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DirectoryChooser dirChooser = new DirectoryChooser();
    private static Tile tile;
    private static List<Media> files;
    private List<String> directories;
    private static boolean autoPlay = false;
    private Users user;
    private List<Tile> tileList;

    @FXML
    Label titleLabel;

    @FXML
    Label timeLabel;

    @FXML
    TextField pathTextField;

    @FXML
    Button sortButton;

    @FXML
    StackPane panel;

    @FXML
    ListView dirList;

    @FXML
    ListView fileList;

    @FXML
    Button autoPlayButton;

    public VideoFolderController(Tile tile){
        user = Helpers.getCurrentUser();
        tileList = user.getTileList();
        if(tileList.get(tileList.size() - 1).getTitle().equals("Add Folder")){
            tileList.remove(tileList.remove(tileList.size() - 1));
        }
        this.tile = tile;
        files = new ArrayList<>();
        directories = new ArrayList<>();
        directories.add("All Videos");
        dirChooser.setTitle("Select Video Directory");
        listAllFiles(tile.getPath());
    }

    @FXML
    public void backToHome(){
        Helpers.switchViews(ViewType.HOMEVIEW, panel);
    }

    @FXML
    public void changeDefault(){
        List<Users> users = Helpers.getAllUsers();

        int indexOfUser = -1;
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getName().equals(user.getName())){
                indexOfUser = i;
                break;
            }
        }

        int indexOfTile = tileList.indexOf(tile);
        if(indexOfTile != -1){
            tile.setPath(pathTextField.getText());
            tileList.set(indexOfTile, tile);
        }

        if(indexOfUser != -1){
            user.setTileList(tileList);
            users.set(indexOfUser, user);
            Helpers.setCurrentUser(user);
            Helpers.setLastuser(user);
            Helpers.setUsers(users);
            Helpers.writeUserJSON(user);
            Helpers.writeUsersJSON(users);
        }
    }

    @FXML
    public void uploadDir(){
        files.clear();
        directories.clear();
        directories.add("All Videos");
        Stage stage = (Stage) panel.getScene().getWindow();
        File file = dirChooser.showDialog(stage);

        if(file != null)
        {
            pathTextField.setText(file.getAbsolutePath());
        }
        listAllFiles(file.getAbsolutePath());
        dirList.getSelectionModel().selectedItemProperty().addListener(new DirectoryChangeListener(fileList, pathTextField.getText()));
        dirChooser.setInitialDirectory(file.getAbsoluteFile());
        updateListView();
    }

    @FXML
    public void sortFiles(){
        List<Media> list = files.size() == 0 ? DirectoryChangeListener.getVideoList() : files;
        int j = list.size() - 1;
        for(int i = 0; i < list.size() / 2; i++){
            Media tmp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, tmp);
            j--;
        }

        this.fileList.getItems().clear();
        ObservableList<Media> file$ = FXCollections.observableArrayList(list);
        this.fileList.setItems(file$);
    }

    @FXML
    public void toggleAutoPlay(){
        autoPlay = !autoPlay;
        if(autoPlay){
            autoPlayButton.setText("Auto Play: ON");
        } else{
            autoPlayButton.setText("Auto Play: OFF");
        }
    }

    public void listAllFiles(String path)
    {
        File folder = new File(path);
        File[] files = folder.listFiles();
        if(files == null) return;

        for (File f : files)
        {
            if (f.isFile() && isVideoFile(f))
            {
                Media temp = new Media(f.getName(), f.getAbsolutePath(), "/Assets/mediafile.png");
                this.files.add(temp);
            }
            else if (f.isDirectory())
            {
                directories.add(f.getName());
            }
        }
        Collections.sort(this.files, Media.naturalComparator);
    }

    public static boolean isVideoFile(File file){
        String fileName = file.getName();
        if(fileName.matches("^.*\\.(mp4|flv|mkv|avi|mov)$")){
            return true;
        }
        return false;
    }

    public static boolean getAutoPlay(){ return autoPlay; }

    public static Tile getTile(){ return tile; }

    public static List<Media> getFiles(){ return files; }

    public void updateListView(){
        this.dirList.getItems().clear();
        ObservableList<String> dir$ = FXCollections.observableArrayList(directories);
        this.dirList.setCellFactory(x -> new DirectoryFormatCell());
        this.dirList.setItems(dir$);

        this.fileList.getItems().clear();
        ObservableList<Media> file$ = FXCollections.observableArrayList(files);
        this.fileList.setCellFactory(x -> new MediaFormatCell());
        this.fileList.setItems(file$);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(autoPlay){
            autoPlayButton.setText("Auto Play: ON");
        } else{
            autoPlayButton.setText("Auto Play: OFF");
        }
        titleLabel.setText(tile.getTitle());
        pathTextField.setText(tile.getPath());
        dirList.focusedProperty().addListener(new FocusChangeListener(dirList));
        fileList.focusedProperty().addListener(new FocusChangeListener(fileList));
        sortButton.focusedProperty().addListener(new FocusChangeListener(sortButton));
        autoPlayButton.focusedProperty().addListener(new FocusChangeListener(autoPlayButton));
        dirList.getSelectionModel().selectedItemProperty().addListener(new DirectoryChangeListener(fileList, pathTextField.getText()));
        fileList.setOnKeyPressed(new VideoEventHandler(fileList, panel));
        fileList.setOnMousePressed(new VideoEventHandler(fileList, panel));
        updateListView();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                event -> timeLabel.setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}

class FocusChangeListener implements ChangeListener<Boolean>{
    private Node node;

    public FocusChangeListener(Node n) {
        node = n;
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if(newValue)
        {
            node.setOpacity(1);
        }
        else
        {
            node.setOpacity(0.3);
        }
    }
}