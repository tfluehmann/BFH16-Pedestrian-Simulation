package model;

import javafx.scene.layout.Pane;
import manager.PerimeterManager;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Room extends Pane {

	private final PerimeterManager perimeterManager = PerimeterManager.getInstance();
	private final ConfigModel config = ConfigModel.getInstance();

	public Room() {
		this.perimeterManager.initializeAll();
		this.setPrefHeight(config.getRoomHeight());
		this.setPrefWidth(config.getRoomWidth());
		this.setMaxHeight(config.getRoomHeight());
		this.setMaxWidth(config.getRoomWidth());
	}
}
