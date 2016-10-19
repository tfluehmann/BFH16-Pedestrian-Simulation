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

import java.util.*;

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
		for (Position p : o1.getVertices())
			getChildren().add(new Circle(p.getXValue(), p.getYValue(), 2, Color.YELLOW));

		/**
		 * spawn persons randomly or weighted
		 */
		int type;
		if (!config.isWeighted()) {
			for (int i = 0; i < this.config.getTotalPersons(); i++) {
				Random rnd = new Random();
				type = rnd.nextInt(3);
				this.spawnPerson(ga.getGoalPoint(), type);
			}
		} else {
			double personMultiplicator = this.config.getTotalPersons() / 100;
			int young = (int) Math.round(config.getWeightedYoungPersons() * personMultiplicator);
			int midAge = (int) Math.round(config.getWeigthedMidagePersons() * personMultiplicator);
			int old = (int) Math.round(config.getWeightedOldPersons() * personMultiplicator);

			int handicap = (int) Math.round(config.getWeightedHandicappedPersons() * personMultiplicator);
			if (young + midAge + old + handicap != config.getTotalPersons()) {
				handicap = (int) config.getTotalPersons() - young - midAge - old;
			}

//			spawn young persons
			for (int i = 0; i < young; i++) {
				this.spawnPerson(ga.getGoalPoint(), 0);
			}

//			spawn mid age persons
			for (int i = 0; i < midAge; i++) {
				this.spawnPerson(ga.getGoalPoint(), 1);
			}

//			spawn old persons
			for (int i = 0; i < old; i++) {
				this.spawnPerson(ga.getGoalPoint(), 2);
			}

//			spawn handicapped persons
			for (int i = 0; i < handicap; i++) {
				this.spawnPerson(ga.getGoalPoint(), 3);
			}

		}

		this.getChildren().addAll(perimeterManager.getAllNodes());
		getChildren().addAll(this.persons);
	}


	/**
	 * Generating different aged persons randomly
	 * Created by suter1 on 05.10.2016
	 */
	private void spawnPerson(Position goal, int type) {

		Person newPerson;
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
		newPerson.getPathManager().setStartPosition(newPerson.getCurrentPosition());
		newPerson.getPathManager().setTarget(goal);
		newPerson.getPathManager().start();

		getChildren().addAll(newPerson.getPathManager().getEdges());
		this.persons.add(newPerson);
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
					Map<Person, Position> newPositions = new HashMap<>();
					handlePersonsInRange();
					for (Person p : persons) {
						newPositions.put(p, p.calculateStep());
					}
					Platform.runLater(() -> {
						newPositions.forEach((k, v) -> {
							if (v != null) k.setPosition(v);
						});
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
