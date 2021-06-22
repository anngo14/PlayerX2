package Controllers;

import Helpers.*;
import Models.Tile;
import Models.Users;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewTileFolderController implements Controller, Initializable {
    private DirectoryChooser dirChooser = new DirectoryChooser();
    private Users user;
    private List<Tile> tileList;

    @FXML
    TextField titleText;

    @FXML
    ChoiceBox typeChoiceBox;

    @FXML
    TextField pathText;

    @FXML
    StackPane panel;

    public NewTileFolderController(){
        user = Helpers.getCurrentUser();
        tileList = user.getTileList();
        if(tileList.get(tileList.size() - 1).getTitle().equals("Add Folder")){
            tileList.remove(tileList.remove(tileList.size() - 1));
        }
    }

    @FXML
    public void backAction(){
        Helpers.switchViews(ViewType.HOMEVIEW, panel);
    }

    @FXML
    public void choosePath(){
        Stage stage = (Stage) panel.getScene().getWindow();
        File file = dirChooser.showDialog(stage);

        if(file != null)
        {
            pathText.setText(file.getAbsolutePath());
        }
        dirChooser.setInitialDirectory(file.getAbsoluteFile());
    }

    @FXML
    public void save(){
        //Checks all the inputs are filled
        if(titleText.getText().matches("^\\s*$") || pathText.getText().matches("^\\s*$")) return;
        List<Users> users = Helpers.getAllUsers();
        Tile tile = new Tile(titleText.getText(), "/Assets/folder-icon.png", pathText.getText(), ViewType.VIDEOVIEW);

        if(typeChoiceBox.getValue().toString().equals("Music")){
            tile.setViewType(ViewType.MUSICVIEW);
        }

        for(Tile t: tileList){
            //Doesn't add Tiles with duplicate names
            if(t.getTitle().equals(tile.getTitle())){
                return;
            }
        }

        tileList.add(tile);
        int indexOfUser = -1;
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getName().equals(user.getName())){
                indexOfUser = i;
                break;
            }
        }

        if(indexOfUser != -1){
            users.set(indexOfUser, user);
            Helpers.setCurrentUser(user);
            Helpers.setLastuser(user);
            Helpers.setUsers(users);
            Helpers.writeUserJSON(user);
            Helpers.writeUsersJSON(users);
        }
        backAction();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeChoiceBox.getItems().addAll("Video", "Music");
        typeChoiceBox.valueProperty().set("Video");
    }
}
