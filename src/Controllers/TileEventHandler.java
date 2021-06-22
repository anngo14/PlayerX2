package Controllers;

import Helpers.Helpers;
import Helpers.ViewType;
import Models.Tile;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class TileEventHandler implements EventHandler {
    private ListView<Tile> node;
    private StackPane panel;

    public TileEventHandler(Node n, StackPane panel){
        node = (ListView) n;
        this.panel = panel;
    }
    @Override
    public void handle(Event event) {
        Tile select = null;
        if(event.getEventType() == KeyEvent.KEY_PRESSED){
            if(((KeyEvent) event).getCode() == KeyCode.ENTER)
            {
                select = node.getSelectionModel().getSelectedItem();
                ViewType view = select.getViewType();
                Helpers.switchToTilesView(view, panel, select);
            }
        } else if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
            if (((MouseEvent) event).isPrimaryButtonDown() && ((MouseEvent) event).getClickCount() == 2) {
                select = node.getSelectionModel().getSelectedItem();
                ViewType view = select.getViewType();
                Helpers.switchToTilesView(view, panel, select);
            }
        }
    }
}
