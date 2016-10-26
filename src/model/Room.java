package model;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import manager.PathManager;
import manager.PerimeterManager;
import manager.SpawnManager;
import model.areas.Area;
import model.areas.GoalArea;
import model.areas.Obstacle;
import model.areas.SpawnArea;
import model.persons.Person;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Room extends Pane {


	private final Vector<Person> passivePersons = new Vector<>();
	private final PerimeterManager perimeterManager = PerimeterManager.getInstance();
	private final ArrayList<Obstacle> obstacles = new ArrayList();
	private final ConfigModel config = ConfigModel.getInstance();
	private Vector<Person> persons = new Vector<>();
	private Thread simulation;
	private ArrayList<Area> goalAreas;
	private ArrayList<Area> spawnAreas;
	private SpawnManager spawnManager = SpawnManager.getInstance();


	public Room() {
		setPrefSize(this.config.getRoomWidth(), this.config.getRoomHeight());
		this.perimeterManager.initializeAll();

        double x, y;
		if (this.config.getRoomWidth() < ConfigModel.ROOM_WIDTH_ORIGIN)
			x = this.config.getRoomWidth();
		else
            x = 0.0;
		if (this.config.getRoomHeight() < ConfigModel.ROOM_HEIGHT_ORIGIN)
			y = this.config.getRoomHeight();
		else
            y = 0.0;

		if (this.config.getRoomHeight() != ConfigModel.ROOM_HEIGHT_ORIGIN || this.config.getRoomWidth() != ConfigModel.ROOM_WIDTH_ORIGIN) {
			Obstacle border = new Obstacle(x, y,
					ConfigModel.ROOM_WIDTH_ORIGIN, y,
					ConfigModel.ROOM_WIDTH_ORIGIN, ConfigModel.ROOM_HEIGHT_ORIGIN,
					x, ConfigModel.ROOM_HEIGHT_ORIGIN);
			getChildren().add(border);
		}

		SpawnArea sa = new SpawnArea(this.config.getSpawnWidth(), this.config.getSpawnHeight(), this.config.getSpawnPosition());
		GoalArea ga = new GoalArea(this.config.getGoalWidth(), this.config.getGoalHeight(), this.config.getGoalPosition());

		Obstacle o1 = new Obstacle(100.0, 100.0, 450.0, 300.0, 500.0, 450.0, 100.0, 200.0);
		getChildren().addAll(o1, sa, ga);

		this.obstacles.add(o1);
		PathManager pathManager = spawnManager.getPathManager();
		for (Obstacle obstacle : this.obstacles) {
			for (Position p : obstacle.getEdgePoints())
				pathManager.getVertexList().add(new Vertex(p));
			pathManager.getObstacleEdges().addAll(obstacle.getEdges());
		}
		Vertex goal = new Vertex(ga.getGoalPoint());
		pathManager.getVertexList().add(goal);
		pathManager.findValidEdges(this);
		pathManager.findShortestPath(goal);

	}


	/**
	 * shuffle before every run because there might be
	 * unsolvable issues if it is always the same order
	 */
	public void start(Label time) {
		Task task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				int i = 0;
                while (!isSimulationFinished()) {
					handlePersonsInRange();
					Platform.runLater(() -> {
						for (Person p : persons)
							p.calculateStep();
					});

					this.updateMessage(++i + " seconds");
					Thread.sleep(20);
				}
				System.out.println("finished simulation");
				return null;
			}
		};
		time.textProperty().bind(task.messageProperty());

		simulation = new Thread(task);
		simulation.start();
	}

	private void handlePersonsInRange() {
		Vector<Person> newPersons = new Vector<>();
		for (Person p : this.persons)
			if (p.isInGoalArea())
	            this.passivePersons.add(p);
			else
				newPersons.add(p);

		persons = newPersons;
	}

	private boolean isSimulationFinished() {
		for (Person p : this.persons) {
			if (!p.isInGoalArea())
                return false;
		}
		return true;
	}

	public Thread getSimulationThread() {
		return simulation;
	}

}
