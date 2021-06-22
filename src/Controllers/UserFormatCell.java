package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import Models.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class UserFormatCell extends ListCell<Users> {
    private FXMLLoader loader;
    private Users user;
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

    public UserFormatCell(StackPane panel){
        pane = panel;
    }

    @Override
    protected void updateItem(Users u, boolean b) {
        user = u;
        super.updateItem(u, b);
        setStyle("-fx-control-inner-background: rgba(0, 0, 0, 0);");
        if(u == null || b)
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

            userNameText.setText(u.getName());
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
        Helpers.switchViews(ViewType.EDITUSERVIEW, pane);
    }

    public void deleteUser(){
        int input = Helpers.confirmationBox("Are you sure you want to delete this user?");
        if(input == 0)
        {
            Helpers.deleteUser(user);
            if(Helpers.getCurrentUser() == null){
                Helpers.setCurrentUser(new Users());
                Helpers.switchViews(ViewType.NEWUSERVIEW, pane);
            } else{
                Helpers.switchViews(ViewType.SWITCHUSERVIEW, pane);
            }
        }
    }

}
