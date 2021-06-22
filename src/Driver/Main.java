package Driver;

import Controllers.IntroController;
import Helpers.Helpers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Main extends Application {
    private static int count = 0;
    FXMLLoader loader;
    Parent parent;
    Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("PlayerX");
        primaryStage.getIcons().add(new Image("/Assets/Mainicon.png"));
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.valueOf("`"));
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);

        Helpers.setCurrentUser(Helpers.getLastUser());
        Helpers.getAllUsers();

        loader = new FXMLLoader(getClass().getResource(("/Views/IntroView.fxml")));
        loader.setController(new IntroController());
        parent = loader.load();
        scene = new Scene(parent, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/Views/application.css").toExternalForm());
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                count++;
                if(count == 2)
                {
                    int input = Helpers.confirmationBox("Are you sure you want to exit?");
                    if(input == 0)
                    {
                        primaryStage.close();
                    }
                    else
                    {
                        count = 0;
                    }
                }
            }
            if(e.getCode() == KeyCode.CONTROL) {
                if(primaryStage.isFullScreen() == false)
                {
                    primaryStage.setFullScreen(true);
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
