package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import Models.Tile;
import Models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class SettingsController implements Controller, Initializable {
    private Users user;
    private List<Tile> tileList;

    @FXML
    Label userLabel;

    @FXML
    ListView<Tile> list;

    @FXML
    StackPane panel;

    public SettingsController(){
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
    public void addTile(){
        Helpers.switchToTilesView(ViewType.NEWFOLDERVIEW, panel, null);
    }

    public void updateList(){
        list.getItems().clear();
        List<Tile> tiles = new ArrayList<>(tileList);
        if(tiles.size() >= 5){
            tiles = tiles.subList(4, tiles.size());
        } else{
            return;
        }
        ObservableList<Tile> tiles$ = FXCollections.observableArrayList(tiles);
        list.setCellFactory(x -> new EditTileFormatCell(panel));
        list.setItems(tiles$);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userLabel.setText(user.getName());
        updateList();
    }
}
