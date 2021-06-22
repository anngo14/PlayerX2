package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import Models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SwitchUserController implements Controller, Initializable {

    @FXML
    StackPane panel;

    @FXML
    ListView<Users> list;

    public SwitchUserController(){}

    @FXML
    public void addUser(){
        Helpers.setCurrentUser(new Users());
        Helpers.switchViews(ViewType.NEWUSERVIEW, panel);
    }

    @FXML
    public void backToWelcome(){
        Helpers.switchViews(ViewType.WELCOMEVIEW, panel);
    }

    public void updateList()
    {
        list.getItems().clear();
        ObservableList<Users> users$ = FXCollections.observableArrayList(Helpers.getAllUsers());
        list.setCellFactory(userFormatCell -> new UserFormatCell(panel));
        list.setItems(users$);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setOnKeyPressed(new SwitchUserEventHandler(list, panel));
        list.setOnMousePressed(new SwitchUserEventHandler(list, panel));
        updateList();
    }
}
