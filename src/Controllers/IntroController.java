package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import Models.Users;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class IntroController implements Initializable, Controller {
    private static IntroController instance = null;
    FadeTransition fade;

    @FXML
    Text welcomeText;

    @FXML
    StackPane panel;

    public IntroController(){
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fade = new FadeTransition(Duration.millis(2000), welcomeText);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
        fade.setOnFinished(e -> loadHomeView());
        fade.play();
    }

    private void loadHomeView() {
        if(Helpers.getLastUser() == null){
            Helpers.setCurrentUser(new Users());
            Helpers.switchViews(ViewType.NEWUSERVIEW, panel);
        } else{
            Helpers.switchViews(ViewType.WELCOMEVIEW, panel);
        }
    }
}
