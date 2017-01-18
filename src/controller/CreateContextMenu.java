package controller;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import manager.areamanagers.GoalAreaManager;
import manager.areamanagers.ObstacleManager;
import manager.areamanagers.SpawnAreaManager;
import model.ConfigModel;
import model.Room;
import model.areas.GoalArea;
import model.areas.Obstacle;
import model.areas.SpawnArea;

/**
 * Created by fluht1 on 18/01/17.
 * Creates the context menu to create obstacles and other areas
 */
class CreateContextMenu extends ContextMenu {

    CreateContextMenu(GoalAreaManager goalAreaManager, SpawnAreaManager spawnAreaManager,
                      ObstacleManager obstacleManager, ConfigModel configModel, Room simulationRoom) {
        ContextMenu cm = new ContextMenu();
        MenuItem goalItem = new MenuItem("Goal area");
        MenuItem spawnItem = new MenuItem("Spawn area");
        Menu obstacleMenu = new Menu("Obstacles");

        goalItem.setOnAction((e) -> goalAreaManager.add(GoalArea.createWithNEdges(4, GoalArea.class)));
        spawnItem.setOnAction((e) -> spawnAreaManager.add(SpawnArea.createWithNEdges(4, SpawnArea.class)));

        String[] polygonNames = {"Triangle", "Rectangle", "Pentagon", "Hexagon", "Heptagon", "Octagon"};
        for (int corners = configModel.getMinObstacleCorners(), i = 0;
             corners <= configModel.getMaxObstacleCorners(); corners++, i++) {
            MenuItem item = new MenuItem(polygonNames[i]);
            final int cornerCount = corners;
            item.setOnAction((e) -> obstacleManager.add(Obstacle.createWithNEdges(cornerCount, Obstacle.class)));
            obstacleMenu.getItems().add(item);
        }

        cm.getItems().addAll(goalItem, spawnItem, obstacleMenu);

        simulationRoom.setOnMouseClicked((event) -> {
            if (event.getButton().toString().equals("SECONDARY"))
                cm.show(simulationRoom, event.getScreenX(), event.getSceneY());
        });
    }
}
