package model;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import manager.PathManager;
import manager.PerimeterManager;
import model.areas.Area;
import model.areas.GoalArea;
import model.areas.Obstacle;
import model.areas.SpawnArea;
import model.persons.Person;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
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
	private Random rnd = new Random();


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
		PathManager pathManager = new PathManager();
		for (Obstacle obstacle : this.obstacles) {
			for (Position p : obstacle.getEdgePoints())
				pathManager.getVertexList().add(new Vertex(p));
			pathManager.getObstacleEdges().addAll(obstacle.getEdges());
		}
		Vertex goal = new Vertex(ga.getGoalPoint());
		pathManager.getVertexList().add(goal);
		pathManager.findValidEdges(this);
		pathManager.findShortestPath(goal);

		/**
		 * spawn persons randomly or weighted
		 */
		try {
			Class[] personTypes = {Class.forName("model.persons.YoungPerson"),
					Class.forName("model.persons.MidAgePerson"),
					Class.forName("model.persons.OldPerson"),
					Class.forName("model.persons.HandicappedPerson")};

			if (!config.isWeighted()) {
				for (int i = 0; i < this.config.getTotalPersons(); i++) {
					int type = rnd.nextInt(3);
					this.spawnPerson(pathManager, personTypes[type]);
				}
			} else {
				double personMultiplicator = this.config.getTotalPersons() / 100;
				int[] ageDistribution = new int[4];
				ageDistribution[0] = (int) Math.round(config.getWeightedYoungPersons() * personMultiplicator);
				ageDistribution[1] = (int) Math.round(config.getWeigthedMidagePersons() * personMultiplicator);
				ageDistribution[2] = (int) Math.round(config.getWeightedOldPersons() * personMultiplicator);
				ageDistribution[3] = (int) Math.round(config.getWeightedHandicappedPersons() * personMultiplicator);
				if (ageDistribution[0] + ageDistribution[1] + ageDistribution[2] + ageDistribution[3] != config.getTotalPersons())
					ageDistribution[3] = (int) config.getTotalPersons() - ageDistribution[0] - ageDistribution[1] - ageDistribution[2];

				for (int i = 0; i < ageDistribution.length; i++)
					createPersons(personTypes[i], ageDistribution[i], pathManager);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		this.getChildren().addAll(perimeterManager.getAllNodes());
		getChildren().addAll(this.persons);
	}

	private void createPersons(Class klass, int count, PathManager pathManager) {
		for (int i = 0; i < count; i++)
			this.spawnPerson(pathManager, klass);
	}


	/**
	 * Generating different aged persons randomly
	 * Created by suter1 on 05.10.2016
	 */
	private void spawnPerson(PathManager pathManager, Class<? extends Person> klass) {

		Person newPerson;
		try {
			Class partypes[] = new Class[3];
			partypes[0] = Double.TYPE;
			partypes[1] = Double.TYPE;
			partypes[2] = Position.class;
			Constructor ct = klass.getConstructor(partypes);
			newPerson = (Person) ct.newInstance(this.config.getSpawnHeight(), this.config.getSpawnWidth(), this.config.getSpawnPosition());
			this.persons.add(newPerson);
			newPerson.setPath(pathManager.getShortestPathFromPosition(newPerson.getCurrentPosition()));
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}
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
