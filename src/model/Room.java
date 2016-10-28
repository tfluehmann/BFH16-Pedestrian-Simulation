package model;

import javafx.scene.layout.Pane;
import manager.PathManager;
import manager.PerimeterManager;
import manager.SpawnManager;
import model.areas.Area;
import model.areas.GoalArea;
import model.areas.Obstacle;
import model.areas.SpawnArea;

import java.util.ArrayList;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Room extends Pane {


	private final PerimeterManager perimeterManager = PerimeterManager.getInstance();
	private final ArrayList<Obstacle> obstacles = new ArrayList();
	private final ConfigModel config = ConfigModel.getInstance();
	private ArrayList<Area> goalAreas;
	private ArrayList<Area> spawnAreas;
	private SpawnManager spawnManager = SpawnManager.getInstance();


	public Room() {
		//	setPrefSize(this.config.getRoomWidth(), this.config.getRoomHeight());
		this.perimeterManager.initializeAll();
		this.setStyle("fx-border-color: gray; -fx-border-style: solid; -fx-border-width: 1px; -fx-padding: 1px; -fx-stroke: gray;");
		this.setPrefHeight(config.getRoomHeight());
		this.setPrefWidth(config.getRoomWidth());
		this.setMaxHeight(config.getRoomHeight());
		this.setMaxWidth(config.getRoomWidth());
		SpawnArea sa = new SpawnArea(this.config.getSpawnWidth(), this.config.getSpawnHeight(), this.config.getSpawnPosition());
		GoalArea ga = new GoalArea(this.config.getGoalWidth(), this.config.getGoalHeight(), this.config.getGoalPosition());

		Obstacle o1 = new Obstacle(100.0, 100.0, 450.0, 300.0, 500.0, 450.0, 100.0, 200.0);
		getChildren().addAll(o1, sa, ga);

		this.obstacles.add(o1);
		Vertex goal = new Vertex(ga.getGoalPoint());
		PathManager pathManager = spawnManager.getPathManager();
        pathManager.addTarget(goal);
        for (Obstacle obstacle : this.obstacles) {
			for (Position p : obstacle.getEdgePoints())
				pathManager.getVertexList().add(new Vertex(p));
			pathManager.getObstacleEdges().addAll(obstacle.getEdges());
		}
		pathManager.findValidEdges(this);
		pathManager.crapFindAlgorithm(goal);
	}
}
