package manager;

import manager.areamanagers.SpawnAreaManager;
import model.ConfigModel;
import model.Position;
import model.areas.SpawnArea;
import model.persons.Person;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 21.10.2016.
 */
public class SpawnManager {
	private static SpawnManager instance = null;
	private final Vector<Person> passivePersons = new Vector<>();
	private SpawnAreaManager spawnAreaManager = SpawnAreaManager.getInstance();
	private ConfigModel config = ConfigModel.getInstance();
	private Random rnd = new Random();
	private Vector<Person> persons = new Vector<>();
	private PathManager pathManager = new PathManager();


	private SpawnManager() {
	}


	public static SpawnManager getInstance() {
		if (instance == null) instance = new SpawnManager();
		return instance;
	}


	public void createPersons() {
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
					int type = rnd.nextInt(personTypes.length);
					this.spawnPerson(pathManager, personTypes[type]);
				}
			} else {
				double personMultiplicator = this.config.getTotalPersons() / 100;
				int[] ageDistribution = new int[4];
				ageDistribution[0] = (int) Math.round(config.getWeightedYoungPersons() * personMultiplicator);
				ageDistribution[1] = (int) Math.round(config.getWeightedMidAgePersons() * personMultiplicator);
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
			Class partypes[] = new Class[1];
//			partypes[0] = Double.TYPE;
//			partypes[1] = Double.TYPE;
			partypes[0] = Position.class;
			Constructor ct = klass.getConstructor(partypes);
			SpawnArea spawnArea = spawnAreaManager.getSpawnAreas().get(ThreadLocalRandom.current().nextInt(0, spawnAreaManager.getSpawnAreas().size()));
			newPerson = (Person) ct.newInstance(getSpawnPosition(spawnArea)); // TODO width, heigth, position
			this.persons.add(newPerson);
			newPerson.setNextVertex(pathManager.getClosestVertex(newPerson.getCurrentPosition()));
			newPerson.setTarget(pathManager.getTargetVertexes().get(0));
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}


	private Position getSpawnPosition(SpawnArea spawn) {
		Random r = new Random();
		double x, y;
		double randomWidth, randomHeight;
		do {
			randomWidth = (spawn.getBoundsInLocal().getWidth() - (config.getPersonRadius() * 2)) * r.nextDouble();
			randomHeight = (spawn.getBoundsInLocal().getHeight() - (config.getPersonRadius() * 2)) * r.nextDouble();
			x = spawn.getPosition().getXValue() + randomWidth;
			y = spawn.getPosition().getYValue() + randomHeight;
		}
		while (!spawn.contains(x + config.getPersonRadius(), y + config.getPersonRadius()) || !spawn.contains(x - config.getPersonRadius(), y - config.getPersonRadius())
				|| !spawn.contains(x + config.getPersonRadius(), y - config.getPersonRadius()) || !spawn.contains(x - config.getPersonRadius(), y + config.getPersonRadius()));
		return new Position(x, y);
	}

	public Vector<Person> getPersons() {
		return this.persons;
	}


	public PathManager getPathManager() {
		return this.pathManager;
	}


	public void handlePersonsInTargetRange() {
		Vector<Person> newPersons = new Vector<>();
		for (Person p : this.persons)
			if (p.isInGoalArea())
				this.passivePersons.add(p);
			else
				newPersons.add(p);
		this.persons = newPersons;
	}


	public void clear() {
		this.passivePersons.clear();
		this.persons.clear();
		this.pathManager.clear();
	}


	public Vector<Person> getPassivePersons() {
		return this.passivePersons;
	}
}
