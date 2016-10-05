package model;




import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import model.areas.*;
import model.persons.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Room extends Pane {

	private List<Person> persons = new ArrayList<>();
	private List<Person> passivePersons = new ArrayList<>();


	private List<Perimeter> perimeters = new LinkedList<>();
	private ArrayList<Area> obstacles;
	private ArrayList<Area> goalAreas;
	private ArrayList<Area> spawnAreas;

	public static final int ROOM_HEIGHT = 400;
	public static final int ROOM_WIDTH = 400;

	public static final int GOAL_HEIGHT = 20;
	public static final int GOAL_WIDTH = 50;

	public static final int SPAWN_HEIGHT = 150;
	public static final int SPAWN_WIDTH = 150;
	public Room() throws InterruptedException {
		super();
		this.setPrefSize(ROOM_WIDTH, ROOM_HEIGHT);
		perimeters.addAll(Perimeter.initializeAll(ROOM_WIDTH, ROOM_HEIGHT));
		SpawnArea sa = new SpawnArea(SPAWN_WIDTH, SPAWN_HEIGHT, new Position(0.0, 0.0));
		GoalArea ga = new GoalArea(GOAL_WIDTH, GOAL_HEIGHT, new Position(350.0, 380.0));

		this.getChildren().add(sa);
		this.getChildren().add(ga);
		List<Position> edges = ga.getEdges();
		edges.add(0, new Position(0, 300));
		edges.add(0, new Position(300, 0));

		/**
		 * Generating different aged persons randomly
		 * Created by suter1 on 05.10.2016
		 */
		Random rnd = new Random();
		int type = 0;
		for (int i = 0; i < 500; i++) {
			type = rnd.nextInt(3);
			switch (type) {
				case 0:
					persons.add(new YoungPerson(SPAWN_HEIGHT, SPAWN_WIDTH, edges));
					break;
				case 1:
					persons.add(new MidAgePerson(SPAWN_HEIGHT, SPAWN_WIDTH, edges));
					break;
				case 2:
					persons.add(new OldPerson(SPAWN_HEIGHT, SPAWN_WIDTH, edges));
					break;
				case 3:
					persons.add(new HandycappedPerson(SPAWN_HEIGHT, SPAWN_WIDTH, edges));
					break;
				default:
					persons.add(new MidAgePerson(SPAWN_HEIGHT, SPAWN_WIDTH, edges));
					break;
			}
			//persons.add(new MidAgePerson(SPAWN_WIDTH, SPAWN_HEIGHT, edges));
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
						for(Person p : persons){
							Position nextPos = p.nextPosition();
							p.setPosition(nextPos);
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
