package model;

import config.ConfigModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import manager.PerimeterManager;
import model.areas.Area;
import model.areas.GoalArea;
import model.areas.Obstacle;
import model.areas.SpawnArea;
import model.persons.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Room extends Pane {


	private final List<Person> passivePersons = new ArrayList<>();
	private final PerimeterManager perimeterManager = PerimeterManager.getInstance();
	private final ArrayList<Obstacle> obstacles = new ArrayList();
	private final ConfigModel config = ConfigModel.getInstance();
	private List<Person> persons = new ArrayList<>();
	private ArrayList<Area> goalAreas;
	private ArrayList<Area> spawnAreas;


	public Room() {
		setPrefSize(this.config.getRoomWidth(), this.config.getRoomHeight());
		this.perimeterManager.setRoom(this);
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
		for (Position p : o1.getVertices())
			getChildren().add(new Circle(p.getXValue(), p.getYValue(), 2, Color.YELLOW));

		for (int i = 0; i < this.config.getTotalPersons(); i++)
			this.spawnPerson(ga.getGoalPoint());

		//this.getChildren().addAll(perimeterManager.getAllNodes());
		getChildren().addAll(this.persons);
	}

	private void spawnPerson(Position goal) {
		/**
		 * Generating different aged persons randomly
		 * Created by suter1 on 05.10.2016
		 */
		Random rnd = new Random();
		int type;
			Person newPerson;
			type = rnd.nextInt(3);
			switch (type) {
				case 0:
					newPerson = new YoungPerson(this.config.getSpawnHeight(), this.config.getSpawnWidth(), this.config.getSpawnPosition());
					break;
				case 1:
					newPerson = new MidAgePerson(this.config.getSpawnHeight(), this.config.getSpawnWidth(), this.config.getSpawnPosition());
					break;
				case 2:
					newPerson = new OldPerson(this.config.getSpawnHeight(), this.config.getSpawnWidth(), this.config.getSpawnPosition());
					break;
				case 3:
					newPerson = new HandicappedPerson(this.config.getSpawnHeight(), this.config.getSpawnWidth(), this.config.getSpawnPosition());
					break;
				default:
					newPerson = new MidAgePerson(this.config.getSpawnHeight(), this.config.getSpawnWidth(), this.config.getSpawnPosition());
					break;
			}

		for (Obstacle o : this.obstacles) {
			newPerson.getPathManager().getVertices().addAll(o.getVertices());
			newPerson.getPathManager().getObstacleEdges().addAll(o.getEdges());
		}

		newPerson.getPathManager().getVertices().add(goal);
		newPerson.getPathManager().getVertices().add(newPerson.getCurrentPosition());
		this.perimeterManager.registerPerson(newPerson);
		newPerson.getPathManager().findValidEdges();

		newPerson.getPathManager().findShortestPath(newPerson.getCurrentPosition());
		List<Position> positions = newPerson.getPathManager().getPath(goal);
		newPerson.getPath().addAll(positions);
		System.out.println(newPerson.getPath());
		getChildren().addAll(newPerson.getPathManager().getEdges());
		this.persons.add(newPerson);


	}

	public void start(Label time) {
		Task task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				int i = 0;
				while (!Room.this.isSimulationFinished()) {
					Room.this.handlePersonsInRange();
					Platform.runLater(() -> {
						/**
						 * shuffle before every run because there might be
						 * unsolvable issues if it is always the same order
						 */
						long seed = System.nanoTime();
						Collections.shuffle(Room.this.persons, new Random(seed));
						Room.this.persons.forEach(Person::doStep);
					});
					this.updateMessage(++i + " seconds");
					Thread.sleep(30);
				}
				System.out.println("finished simulation");
				return null;
			}
		};
		time.textProperty().bind(task.messageProperty());

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}

	private void handlePersonsInRange() {
		ArrayList<Person> newPersons = new ArrayList<>();
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
}
