package model;


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

	public static final int ROOM_HEIGHT = 400;
	public static final int ROOM_WIDTH = 400;
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
		this.setPrefSize(ROOM_WIDTH, ROOM_HEIGHT);
		perimeterManager.setRoom(this);
		perimeterManager.initializeAll();
		SpawnArea sa = new SpawnArea(SPAWN_WIDTH, SPAWN_HEIGHT, new Position(0.0, 0.0));
		GoalArea ga = new GoalArea(GOAL_WIDTH, GOAL_HEIGHT, new Position(350.0, 380.0));

		Obstacle o1 = new Obstacle(100.0, 200.0, 150.0, 200.0, 150.0, 220.0, 100.0, 250.0);

		this.getChildren().add(o1);

		this.getChildren().add(sa);
		this.getChildren().add(ga);
		List<Position> edges = ga.getEdges();
        for(Area o : obstacles)
            edges.addAll(o.getEdges());

		for (Position p : o1.getExtendetEdges()) {
			this.getChildren().add(new Circle(p.getXValue(), p.getYValue(), 2, Color.YELLOW));
		}

		/**
		 * Generating different aged persons randomly
		 * Created by suter1 on 05.10.2016
		 */
		Random rnd = new Random();
		int type;
		for (int i = 0; i < 20; i++) {
			Person p;
			type = rnd.nextInt(3);
			switch (type) {
				case 0:
					p = new YoungPerson(SPAWN_HEIGHT, SPAWN_WIDTH, edges);
					break;
				case 1:
					p = new MidAgePerson(SPAWN_HEIGHT, SPAWN_WIDTH, edges);
					break;
				case 2:
					p = new OldPerson(SPAWN_HEIGHT, SPAWN_WIDTH, edges);
					break;
				case 3:
					p = new HandycappedPerson(SPAWN_HEIGHT, SPAWN_WIDTH, edges);
					break;
				default:
					p = new MidAgePerson(SPAWN_HEIGHT, SPAWN_WIDTH, edges);
					break;
			}
			perimeterManager.registerPerson(p);
			persons.add(p);
		}
		this.getChildren().addAll(persons);
	}

	public void start(Label time){

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
						for(Person p : persons){
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
		for(Person p : persons)
			if (p.isInGoalArea())
				passivePersons.add(p);
			else
				newPersons.add(p);

		this.persons = newPersons;
	}

	private boolean isSimulationFinished() {
		for(Person p : persons){
			if(!p.isInGoalArea())
				return false;
		}
		return true;
	}
}
