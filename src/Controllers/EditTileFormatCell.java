package Controllers;

import Helpers.*;
import Models.Tile;
import Models.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class EditTileFormatCell extends ListCell<Tile> {
    private FXMLLoader loader;
    private Tile tile;
    private StackPane pane;

    @FXML
    Text userNameText;

    @FXML
    Button editBtn;

    @FXML
    Button deleteBtn;

    @FXML
    ImageView img;

    @FXML
    HBox panel;

    public EditTileFormatCell(StackPane panel){
        pane = panel;
    }

    @Override
    protected void updateItem(Tile t, boolean b) {
        tile = t;
        super.updateItem(t, b);
        setStyle("-fx-control-inner-background: rgba(0, 0, 0, 0);");
        if(t == null || b)
        {
            setText(null);
            setGraphic(null);
        }
        else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("/Views/ListCellUserView.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            img.setImage(new Image(String.valueOf(getClass().getResource(tile.getIcon()))));
            userNameText.setText(t.getTitle());
            editBtn.setOnAction(event -> {
                editUser();
            });
            deleteBtn.setOnAction(event -> {
                deleteUser();
            });

            setText(null);
            setGraphic(panel);
        }
    }

    public void editUser(){
        Helpers.switchToTilesView(ViewType.EDITFOLDERVIEW, pane, tile);
    }

    public void deleteUser(){
        int input = Helpers.confirmationBox("Are you sure you want to delete this tile?");
        if(input == 0)
        {
            Users user = Helpers.getCurrentUser();
            List<Tile> tileList = user.getTileList();
            List<Users> users = Helpers.getAllUsers();

            int indexOfTile = -1;
            for(int i = 0; i < tileList.size(); i++){
                if(tileList.get(i).getTitle().equals(this.tile.getTitle())){
                    indexOfTile = i;
                    break;
                }
            }

            tileList.remove(indexOfTile);
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
            Helpers.switchViews(ViewType.SETTINGSVIEW, pane);
        }
    }

}
