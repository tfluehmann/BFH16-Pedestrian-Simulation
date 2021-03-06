package manager;

import manager.areamanagers.SpawnAreaManager;
import model.ConfigModel;
import model.Position;
import model.TargetVertex;
import model.Vertex;
import model.areas.SpawnArea;
import model.persons.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 21.10.2016.
 * The spawnmanager handles the initial placement of the persons
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
        /*
         * spawn persons randomly or weighted
		 */
        Class[] personTypes = {
                YoungPerson.class, MidAgePerson.class, OldPerson.class, HandicappedPerson.class
        };

        if (!config.isWeighted()) {
            for (int i = 0; i < this.config.getTotalPersons(); i++) {
                int type = rnd.nextInt(personTypes.length);
                this.spawnPerson(pathManager, personTypes[type]);
            }
        } else {
            double personFactor = this.config.getTotalPersons() / 100;
            int[] ageDistribution = new int[4];
            ageDistribution[0] = (int) Math.round(config.getWeightedYoungPersons() * personFactor);
            ageDistribution[1] = (int) Math.round(config.getWeightedMidAgePersons() * personFactor);
            ageDistribution[2] = (int) Math.round(config.getWeightedOldPersons() * personFactor);
            ageDistribution[3] = (int) Math.round(config.getWeightedHandicappedPersons() * personFactor);
            if (ageDistribution[0] + ageDistribution[1] + ageDistribution[2] + ageDistribution[3] != config.getTotalPersons())
                ageDistribution[3] = (int) config.getTotalPersons() - ageDistribution[0] - ageDistribution[1] - ageDistribution[2];

            for (int i = 0; i < ageDistribution.length; i++)
                createPersons(personTypes[i], ageDistribution[i], pathManager);
        }
    }

    private void createPersons(Class<? extends Person> _class, int count, PathManager pathManager) {
        for (int i = 0; i < count; i++)
            this.spawnPerson(pathManager, _class);
    }

    /**
     * Generating different aged persons randomly
     * Created by suter1 on 05.10.2016
     */
    private void spawnPerson(PathManager pathManager, Class<? extends Person> _class) {
        Person newPerson;
        try {
            Class partypes[] = new Class[1];
            partypes[0] = Position.class;
            Constructor ct = _class.getConstructor(partypes);
            SpawnArea spawnArea = spawnAreaManager.getSpawnAreas().get(ThreadLocalRandom.current().nextInt(0, spawnAreaManager.getSpawnAreas().size()));
            newPerson = (Person) ct.newInstance(getSpawnPosition(spawnArea));
            this.persons.add(newPerson);
            Vertex nextHop = pathManager.getClosestVertex(newPerson.getCurrentPosition());
            newPerson.setNextVertex(nextHop);
            if (nextHop instanceof TargetVertex) newPerson.setTarget((TargetVertex) nextHop);
            else newPerson.setTarget(nextHop.getShortestTarget());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * calculate possible spawn position in a selected SpawnArea
     *
     * @param spawn spawnarea to give the boundaries where to place the start
     *
     * @return Position
     */
    private Position getSpawnPosition(SpawnArea spawn) {
        Random r = new Random();
        double x, y;
        double randomWidth, randomHeight;
        do {
            randomWidth = (spawn.getBoundsInLocal().getWidth() - (config.getPersonRadius() * 2)) * r.nextDouble();
            randomHeight = (spawn.getBoundsInLocal().getHeight() - (config.getPersonRadius() * 2)) * r.nextDouble();
            x = spawn.getBoundsInLocal().getMinX() + randomWidth;
            y = spawn.getBoundsInLocal().getMinY() + randomHeight;
        }
        while (!spawn.contains(x + config.getPersonRadius(), y + config.getPersonRadius())
                || !spawn.contains(x - config.getPersonRadius(), y - config.getPersonRadius())
                || !spawn.contains(x + config.getPersonRadius(), y - config.getPersonRadius())
                || !spawn.contains(x - config.getPersonRadius(), y + config.getPersonRadius()));
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
            if (p.isInGoal()) {
                this.passivePersons.add(p);
            } else
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
