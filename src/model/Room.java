package model;


import config.ConfigModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import manager.PathManager;
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
    private PathManager pathManager = PathManager.getInstance();
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

        Obstacle border = new Obstacle(x, y,
                config.ROOM_WIDTH_ORIGIN, y,
                config.ROOM_WIDTH_ORIGIN, config.ROOM_HEIGHT_ORIGIN,
                x, config.ROOM_HEIGHT_ORIGIN);

        System.out.println("Room-correction: " + border.toString());

        SpawnArea sa = new SpawnArea(SPAWN_WIDTH, SPAWN_HEIGHT, new Position(0.0, 0.0));
        GoalArea ga = new GoalArea(GOAL_WIDTH, GOAL_HEIGHT, new Position(config.getRoomWidth() - GOAL_WIDTH, config.getRoomHeight() - GOAL_HEIGHT));

        Obstacle o1 = new Obstacle(100.0, 200.0, 400.0, 200.0, 150.0, 220.0, 100.0, 250.0);
        this.getChildren().addAll(border, o1, sa, ga);

        pathManager.getVertices().addAll(o1.getVertices());

        pathManager.getVertices().add(ga.getCurrentPosition());
        pathManager.getObstacleEdges().addAll(o1.getEdges());
        for (Position p : o1.getVertices())
            this.getChildren().add(new Circle(p.getXValue(), p.getYValue(), 2, Color.YELLOW));

        /**
         * Generating different aged persons randomly
         * Created by suter1 on 05.10.2016
         */
        Random rnd = new Random();
        int type;
        for (int i = 0; i < config.getTotalPersons(); i++) {
            Person newPerson;
            type = rnd.nextInt(3);
            switch (type) {
                case 0:
                    newPerson = new YoungPerson(SPAWN_HEIGHT, SPAWN_WIDTH);
                    break;
                case 1:
                    newPerson = new MidAgePerson(SPAWN_HEIGHT, SPAWN_WIDTH);
                    break;
                case 2:
                    newPerson = new OldPerson(SPAWN_HEIGHT, SPAWN_WIDTH);
                    break;
                case 3:
                    newPerson = new HandicappedPerson(SPAWN_HEIGHT, SPAWN_WIDTH);
                    break;
                default:
                    newPerson = new MidAgePerson(SPAWN_HEIGHT, SPAWN_WIDTH);
                    break;
            }
            pathManager.getVertices().add(newPerson.getCurrentPosition());
            perimeterManager.registerPerson(newPerson);
            persons.add(newPerson);
        }

        pathManager.findValidEdges();

        for (Person pers : persons) {
            pathManager.findShortestPath(pers.getCurrentPosition());
            List<Position> positions = pathManager.getPath(ga.getCurrentPosition());
            System.out.println("correct way found: " + positions.size());
            pers.getPath().addAll(positions);
        }

        this.getChildren().addAll(pathManager.getEdges());
        this.getChildren().addAll(pathManager.getObstacleEdges());
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