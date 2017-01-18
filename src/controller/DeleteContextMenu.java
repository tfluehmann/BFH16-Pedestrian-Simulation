package controller;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import manager.areamanagers.AreaManager;
import model.areas.Area;

/**
 * Created by tgdflto1 on 18/01/17.
 * ContextMenu that appears when you right-click on an area
 */
public class DeleteContextMenu extends ContextMenu {

    public DeleteContextMenu(AreaManager manager, Area area) {
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction((event -> manager.remove(area)));
        this.getItems().add(deleteItem);
        this.setStyle("-fx-background-color: #1d1d1d");
        deleteItem.setStyle("-fx-text-fill: #fff");
        area.setOnMouseClicked((event) -> {
            if (event.getButton().toString().equals("SECONDARY")) {
                this.show(area, event.getScreenX(), event.getSceneY());
                event.consume();
            }
        });
    }
}
