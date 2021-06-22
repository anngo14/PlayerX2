package Controllers;


import Helpers.*;
import Models.Users;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class SwitchUserEventHandler implements EventHandler {
    private ListView<Users> node;
    private StackPane panel;

    public SwitchUserEventHandler(Node n, StackPane panel){
        node = (ListView) n;
        this.panel = panel;
    }

    @Override
    public void handle(Event event) {
        Users select = null;
        if(event.getEventType() == KeyEvent.KEY_PRESSED){
            if(((KeyEvent) event).getCode() == KeyCode.ENTER)
            {
                select = node.getSelectionModel().getSelectedItem();
                Helpers.writeUserJSON(select);
                Helpers.switchViews(ViewType.WELCOMEVIEW, panel);
            }
        } else if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
            if (((MouseEvent) event).isPrimaryButtonDown() && ((MouseEvent) event).getClickCount() == 2) {
                select = node.getSelectionModel().getSelectedItem();
                Helpers.writeUserJSON(select);
                Helpers.switchViews(ViewType.WELCOMEVIEW, panel);
            }
        }
    }
}
