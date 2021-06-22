package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable, Controller {

    @FXML
    StackPane panel;

    @FXML
    Text userNameText;

    public WelcomeController(){
    }

    @FXML
    private void switchUser(){
        Helpers.switchViews(ViewType.SWITCHUSERVIEW, panel);
    }

    @FXML
    private void exitApplication(){
        int input = Helpers.confirmationBox("Are you sure you want to exit?");
        if(input == 0)
        {
            System.exit(0);
        }
        else {
            return;
        }
    }

    @FXML
    private void goToHome(){
        Helpers.switchViews(ViewType.HOMEVIEW, panel);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Helpers.getCurrentUser() == null){
            Helpers.switchViews(ViewType.NEWUSERVIEW, panel);
        }
        userNameText.setText(Helpers.getCurrentUser().getName());
    }
}
