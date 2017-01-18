package model;

import javafx.scene.layout.Pane;
import manager.PerimeterManager;

/**
 * Created by fluth1 on 30/09/16.
 * Simulation room
 */
public class Room extends Pane {

	public Room() {
		PerimeterManager perimeterManager = PerimeterManager.getInstance();
		perimeterManager.initializeAll();
		ConfigModel config = ConfigModel.getInstance();
		this.setPrefHeight(config.getRoomHeight());
		this.setPrefWidth(config.getRoomWidth());
		this.setMaxHeight(config.getRoomHeight());
		this.setMaxWidth(config.getRoomWidth());
	}
}
