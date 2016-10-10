package model;


import config.ConfigModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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

	public static final int GOAL_HEIGHT = 20;
	public static final int GOAL_WIDTH = 50;
	public static final int SPAWN_HEIGHT = 150;
	public static final int SPAWN_WIDTH = 150;
	private List<Person> persons = new ArrayList<>();
	private List<Person> passivePersons = new ArrayList<>();
	private PerimeterManager perimeterManager = PerimeterManager.getInstance();
	private ArrayList<Area> obstacles = new ArrayList();
	private ArrayList<Area> goalAreas;
	private ArrayList<Area> spawnAreas;


	public Room() throws InterruptedException {
		super();
		ConfigModel config = ConfigModel.getInstance();
		this.setPrefSize(config.getRoomWidth(), config.getRoomHeight());
		perimeterManager.setRoom(this);
		perimeterManager.initializeAll();

		double x, y;
		if (config.getRoomWidth() < config.ROOM_WIDTH_ORIGIN)
			x = config.getRoomWidth();
		else
			x = 0.0;
		if (config.getRoomHeight() < config.ROOM_HEIGHT_ORIGIN)
			y = config.getRoomHeight();
		else
			y = 0.0;

		if (config.getRoomHeight() != config.ROOM_HEIGHT_ORIGIN || config.getRoomWidth() != config.ROOM_WIDTH_ORIGIN) {
			Obstacle border = new Obstacle(x, y,
					config.ROOM_WIDTH_ORIGIN, y,
					config.ROOM_WIDTH_ORIGIN, config.ROOM_HEIGHT_ORIGIN,
					x, config.ROOM_HEIGHT_ORIGIN);
			this.getChildren().add(border);
		}

		SpawnArea sa = new SpawnArea(config.getSpawnWidth(), config.getSpawnHeight(), config.getSpawnPosition());
		GoalArea ga = new GoalArea(config.getGoalWidth(), config.getGoalHeight(), config.getGoalPosition());

//		Obstacle o1 = new Obstacle(100.0, 200.0, 150.0, 200.0, 150.0, 220.0, 100.0, 250.0);
		this.getChildren().addAll(sa, ga);

		List<Position> edges = ga.getCorners();
		for (Area o : obstacles)
			edges.addAll(o.getCorners());

//		for (Position p : o1.getCorners()) {
//			this.getChildren().add(new Circle(p.getXValue(), p.getYValue(), 2, Color.YELLOW));
//		}

		/**
		 * Generating different aged persons randomly
		 * Created by suter1 on 05.10.2016
		 */
		Random rnd = new Random();
		int type;
		for (int i = 0; i < config.getTotalPersons(); i++) {
			Person p;
			type = rnd.nextInt(3);
			switch (type) {
				case 0:
					p = new YoungPerson(config.getSpawnHeight(), config.getSpawnWidth(), edges, config.getSpawnPosition());
					break;
				case 1:
					p = new MidAgePerson(config.getSpawnHeight(), config.getSpawnWidth(), edges, config.getSpawnPosition());
					break;
				case 2:
					p = new OldPerson(config.getSpawnHeight(), config.getSpawnWidth(), edges, config.getSpawnPosition());
					break;
				case 3:
					p = new HandycappedPerson(config.getSpawnHeight(), config.getSpawnWidth(), edges, config.getSpawnPosition());
					break;
				default:
					p = new MidAgePerson(config.getSpawnHeight(), config.getSpawnWidth(), edges, config.getSpawnPosition());
					break;
			}
			perimeterManager.registerPerson(p);
			persons.add(p);
		}
		this.getChildren().addAll(persons);
	}


	public void start(Label time) {

		Task task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				int i = 0;
				while (!isSimulationFinished()) {
					handlePersonsInRange();
					Platform.runLater(() -> {
						/**
						 * shuffle before every run because there might be
						 * unsolvable issues if it is always the same order
						 */
						long seed = System.nanoTime();
						Collections.shuffle(persons, new Random(seed));
						for (Person p : persons) {
							p.doStep();
						}
					});
					updateMessage(++i + " seconds");
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
		for (Person p : persons)
			if (p.isInGoalArea())
				passivePersons.add(p);
			else
				newPersons.add(p);

		this.persons = newPersons;
	}


	private boolean isSimulationFinished() {
		for (Person p : persons) {
			if (!p.isInGoalArea())
				return false;
		}
		return true;
	}
}
