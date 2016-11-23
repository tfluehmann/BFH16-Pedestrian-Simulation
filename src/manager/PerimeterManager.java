package manager;

import model.ConfigModel;
import model.Perimeter;
import model.Position;
import model.persons.Person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by tgdflto1 on 05/10/16.
 * manages the grid of perimeters
 */
public class PerimeterManager {
    private static PerimeterManager instance;
    private Vector<Vector<Perimeter>> perimeters;
    private long numberOfPerimetersY;
    private long numberOfPerimetersX;
    private ConfigModel config = ConfigModel.getInstance();
    private double perimeterSize = config.getPersonRadius() * 10;

    private PerimeterManager() {
    }

    public static PerimeterManager getInstance() {
        if (PerimeterManager.instance == null)
            PerimeterManager.instance = new PerimeterManager();
        return PerimeterManager.instance;
    }

    /**
     * create a grid layout from perimeters on the the room
     */
    public void initializeAll() {
        Double height = config.getRoomHeight();
        Double width = config.getRoomWidth();
        this.numberOfPerimetersY = Math.round(height.intValue() / perimeterSize);
        this.numberOfPerimetersX = Math.round(width.intValue() / perimeterSize);
        this.perimeters = new Vector<>();
        for (int i = 0; i < this.numberOfPerimetersX; i++) {
            Vector<Perimeter> currentList = new Vector<>();
            for (int j = 0; j < this.numberOfPerimetersY; j++) {
                currentList.add(new Perimeter(new Position(i * perimeterSize, j * perimeterSize),
                        perimeterSize, perimeterSize, j, i));
            }
            this.perimeters.add(currentList);
        }
    }

    /**
     * get the person's perimeter and register it
     *
     * @param person that will be registred
     */
    public void registerPerson(Person person) {
        Perimeter perimeter = getCurrentPerimeter(person.getCurrentPosition());
        perimeter.register(person);
    }

    /**
     * get all 8 neighbor perimeters (or less in edge cases)
     *
     * @param position the neighbors should be returned
     *
     * @return A set of Neighbor-Perimeters
     */
    public Set<Perimeter> getNeighbors(Position position) {
        Perimeter perimeter = getCurrentPerimeter(position);
        int i = perimeter.getHorizontalArrayPosition() - 1;
        int j = perimeter.getVerticalArrayPosition() - 1;
        Set<Perimeter> neighbors = Collections.synchronizedSet(new HashSet<>());
        for (int a = i; a <= i + 2; a++)
            for (int b = j; b <= j + 2; b++)
                if (a >= 0 && a < this.perimeters.size() && b >= 0 && b < this.perimeters.get(a).size())
                    neighbors.add(this.perimeters.get(a).get(b));
        return neighbors;
    }

    /**
     * remove person if registred in perimeter
     *
     * @param person    want to remove
     * @param perimeter expected to contain the person
     */
    public void unregisterPerson(Person person, Perimeter perimeter) {
        perimeter.unregister(person);
    }

    /**
     * calculate the perimeter based on position
     *
     * @param position from you would like to know the perimeter
     *
     * @return currentPerimeter
     */
    public Perimeter getCurrentPerimeter(Position position) {
        int perimeterI = (int) Math.floor(position.getXValue() / perimeterSize);
        int perimeterJ = (int) Math.floor(position.getYValue() / perimeterSize);
        return perimeters.get(perimeterI).get(perimeterJ);
    }

    /**
     * get all Perimeters in the room
     *
     * @return all perimeters that exist
     */
    public Vector<Perimeter> getAllNodes() {
        Vector<Perimeter> perimetersList = new Vector<>();
        for (int i = 0; i < this.numberOfPerimetersX; i++) {
            for (int j = 0; j < this.numberOfPerimetersY; j++)
                perimetersList.add(this.perimeters.get(i).get(j));
        }
        return perimetersList;
    }
}
