package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import Models.Tile;
import Models.Users;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditUserController implements Controller, Initializable {
    private DirectoryChooser dirChooser = new DirectoryChooser();
    private Users user;

    @FXML
    StackPane panel;

    @FXML
    TextField nameText;

    @FXML
    TextField videoText;

    @FXML
    TextField musicText;

    @FXML
    ImageView backImg;

    public EditUserController(){
        user = Helpers.getCurrentUser();
    }

    @FXML
    public void saveUser(){
        List<Users> users = Helpers.getAllUsers();
        int indexOfUser = -1;
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getName().equals(user.getName())){
                indexOfUser = i;
                break;
            }
        }
        user.setName(nameText.getText());
        if(indexOfUser != -1){
            users.set(indexOfUser, user);
            Helpers.writeUsersJSON(users);
            Helpers.setUsers(users);
            Helpers.setCurrentUser(user);
            Helpers.setLastuser(user);
            Helpers.writeUserJSON(user);
            Helpers.switchViews(ViewType.WELCOMEVIEW, panel);
        }
    }

    @FXML
    public void uploadVideoDir(){
        Stage stage = (Stage) panel.getScene().getWindow();
        File file = dirChooser.showDialog(stage);
        List<Tile> tiles = user.getTileList();
        int indexOfTile = -1;
        Tile videos = null;
        for(int i = 0; i < tiles.size(); i++){
            if(tiles.get(i).getTitle().equals("Videos")){
                indexOfTile = i;
                videos = tiles.get(i);
                break;
            }
        }

        if(file != null)
        {
            videoText.setText(file.getAbsolutePath());
            if(indexOfTile != -1){
                videos.setPath(file.getAbsolutePath());
                tiles.set(indexOfTile, videos);
                user.setTileList(tiles);
            }
        }
    }

    @FXML
    public void uploadMusicDir(){
        Stage stage = (Stage) panel.getScene().getWindow();
        File file = dirChooser.showDialog(stage);
        List<Tile> tiles = user.getTileList();
        int indexOfTile = -1;
        Tile music = null;
        for(int i = 0; i < tiles.size(); i++){
            if(tiles.get(i).getTitle().equals("Music")){
                indexOfTile = i;
                music = tiles.get(i);
                break;
            }
        }
        if(file != null)
        {
            musicText.setText(file.getAbsolutePath());
            if(indexOfTile != -1){
                music.setPath(file.getAbsolutePath());
                tiles.set(indexOfTile, music);
                user.setTileList(tiles);
            }
        }
    }

    @FXML
    public void backAction(){
        Helpers.setCurrentUser(Helpers.getLastUser());
        Helpers.switchViews(ViewType.SWITCHUSERVIEW, panel);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backImg.setImage(new Image(String.valueOf(getClass().getResource("/Assets/Back_Arrow.svg.png"))));
        nameText.setText(user.getName());
    }
}
