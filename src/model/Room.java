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

	private List<Person> persons = new ArrayList<>();
	private List<Person> passivePersons = new ArrayList<>();
    private PerimeterManager perimeterManager = PerimeterManager.getInstance();
	private ArrayList<Obstacle> obstacles = new ArrayList();
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

        Obstacle o1 = new Obstacle(100.0, 200.0, 400.0, 200.0, 150.0, 220.0, 100.0, 250.0);
		this.getChildren().addAll(o1, sa, ga);

		obstacles.add(o1);
		for (Position p : o1.getVertices())
            this.getChildren().add(new Circle(p.getXValue(), p.getYValue(), 2, Color.YELLOW));

		for (int i = 0; i < config.getTotalPersons(); i++)
			spawnPerson(ga.getGoalPoint());

		//this.getChildren().addAll(perimeterManager.getAllNodes());
		this.getChildren().addAll(persons);
	}

	private void spawnPerson(Position goal) {
		/**
		 * Generating different aged persons randomly
		 * Created by suter1 on 05.10.2016
		 */
		Random rnd = new Random();
		ConfigModel config = ConfigModel.getInstance();
		int type;
			Person newPerson;
			type = rnd.nextInt(3);
			switch (type) {
				case 0:
					newPerson = new YoungPerson(config.getSpawnHeight(), config.getSpawnWidth(), config.getSpawnPosition());
					break;
				case 1:
					newPerson = new MidAgePerson(config.getSpawnHeight(), config.getSpawnWidth(), config.getSpawnPosition());
					break;
				case 2:
					newPerson = new OldPerson(config.getSpawnHeight(), config.getSpawnWidth(), config.getSpawnPosition());
					break;
				case 3:
					newPerson = new HandicappedPerson(config.getSpawnHeight(), config.getSpawnWidth(), config.getSpawnPosition());
					break;
				default:
					newPerson = new MidAgePerson(config.getSpawnHeight(), config.getSpawnWidth(), config.getSpawnPosition());
					break;
			}

		for (Obstacle o : obstacles) {
			newPerson.getPathManager().getVertices().addAll(o.getVertices());
			newPerson.getPathManager().getObstacleEdges().addAll(o.getEdges());
		}

		newPerson.getPathManager().getVertices().add(goal);
		newPerson.getPathManager().getVertices().add(newPerson.getCurrentPosition());
			perimeterManager.registerPerson(newPerson);
		newPerson.getPathManager().findValidEdges();

		newPerson.getPathManager().findShortestPath(newPerson.getCurrentPosition());
		List<Position> positions = newPerson.getPathManager().getPath(goal);
		newPerson.getPath().addAll(positions);
		this.getChildren().addAll(newPerson.getPathManager().getEdges());
			persons.add(newPerson);


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
						persons.forEach(Person::doStep);
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
