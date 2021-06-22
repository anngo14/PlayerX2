package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import Models.Media;
import Models.Tile;
import Models.Users;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

public class MusicFolderController implements Controller, Initializable {
    private static DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DirectoryChooser dirChooser = new DirectoryChooser();
    private static Tile tile;
    private static ArrayList<Media> files;
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
    Button autoPlayButton;

    @FXML
    StackPane panel;

    @FXML
    ListView dirList;

    @FXML
    ListView fileList;

    public MusicFolderController(Tile tile){
        this.tile = tile;
        files = new ArrayList<>();
        dirChooser.setTitle("Select Music Directory");
        listAllFiles(tile.getPath());
        user = Helpers.getCurrentUser();
        tileList = user.getTileList();
        if(tileList.get(tileList.size() - 1).getTitle().equals("Add Folder")){
            tileList.remove(tileList.remove(tileList.size() - 1));
        }
    }

    @FXML
    public void toggleAutoPlay() { return; }
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
        Stage stage = (Stage) panel.getScene().getWindow();
        File file = dirChooser.showDialog(stage);

        if(file != null)
        {
            pathTextField.setText(file.getAbsolutePath());
        }
        listAllFiles(file.getAbsolutePath());
        dirChooser.setInitialDirectory(file.getAbsoluteFile());
        updateListView();
    }

    @FXML
    public void sortFiles(){
        int j = files.size() - 1;
        for(int i = 0; i < files.size() / 2; i++){
            Media tmp = files.get(i);
            files.set(i, files.get(j));
            files.set(j, tmp);
            j--;
        }
        updateListView();
    }

    public void listAllFiles(String path)
    {
        File folder = new File(path);
        File[] files = folder.listFiles();
        if(files == null) return;

        for (File f : files)
        {
            if (f.isFile() && isMusicFile(f))
            {
                Media temp = new Media(f.getName(), f.getAbsolutePath(), "/Assets/musicfile.png");
                this.files.add(temp);
            }
            else if (f.isDirectory())
            {
                listAllFiles(f.getAbsolutePath());
            }
        }
        Collections.sort(this.files, Media.naturalComparator);
    }

    public static boolean isMusicFile(File file){
        String fileName = file.getName();
        if(fileName.matches("^.*\\.(mp3|aac|wav)$")){
            return true;
        }
        return false;
    }

    public void updateListView(){
        this.dirList.getItems().clear();
        ObservableList<Media> dir$ = FXCollections.observableArrayList(files.subList(0, files.size() / 2));
        this.dirList.setCellFactory(x -> new MediaFormatCell());
        this.dirList.setItems(dir$);

        this.fileList.getItems().clear();
        ObservableList<Media> file$ = FXCollections.observableArrayList(files.subList(files.size() / 2, files.size()));
        this.fileList.setCellFactory(x -> new MediaFormatCell());
        this.fileList.setItems(file$);
    }

    public static List<Media> getFiles(){ return files; }

    public static Tile getTile(){ return tile; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleLabel.setText(tile.getTitle());
        pathTextField.setText(tile.getPath());
        dirList.setOnKeyPressed(new MusicEventHandler(dirList, panel));
        dirList.setOnMousePressed(new MusicEventHandler(dirList, panel));
        fileList.setOnKeyPressed(new MusicEventHandler(fileList, panel));
        fileList.setOnMousePressed(new MusicEventHandler(fileList, panel));

        dirList.focusedProperty().addListener(new FocusChangeListener(dirList));
        fileList.focusedProperty().addListener(new FocusChangeListener(fileList));
        autoPlayButton.setVisible(false);
        updateListView();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                event -> timeLabel.setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}