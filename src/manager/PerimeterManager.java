package manager;

import config.ConfigModel;
import model.Perimeter;
import model.Position;
import model.persons.Person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by tgdflto1 on 05/10/16.
 */
public class PerimeterManager {
    private static PerimeterManager instance;
    private Vector<Vector<Perimeter>> perimeters;
    private long numberOfPerimetersY;
    private long numberOfPerimetersX;
    ConfigModel config = ConfigModel.getInstance();
    private double perimeterSize = config.getPersonRadius() * 10;

    private PerimeterManager() {
    }

    public static PerimeterManager getInstance(){
	    if (PerimeterManager.instance == null)
		    PerimeterManager.instance = new PerimeterManager();
	    return PerimeterManager.instance;
    }


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
                        perimeterSize, perimeterSize, i, j));
            }
		    this.perimeters.add(currentList);
	    }
    }

    public void registerPerson(Person person){
        Perimeter perimeter = getCurrentPerimeter(person.getCurrentPosition());
        perimeter.register(person);
        if (!perimeter.isInRange(person.getCurrentPosition())) throw new RuntimeException("fail does not conclude it");
    }

    public Set<Perimeter> getNeighbors(Position position) {
        Perimeter perimeter = getCurrentPerimeter(position);
        int i = perimeter.getHorizontalArrayPosition() - 1;
        int j = perimeter.getVerticalArrayPosition() - 1;
        Set<Perimeter> neighbors = Collections.synchronizedSet(new HashSet<>());
        for (int a = i; a <= i + 2; a++)
		    for (int b = j; b <= j + 2; b++)
                if (i >= 0 && i < this.perimeters.size() && j >= 0 && j < this.perimeters.get(i).size())
                    neighbors.add(this.perimeters.get(i).get(j));
	    return neighbors;
    }

    public void unregisterPerson(Person person, Perimeter perimeter) {
        if (perimeter.getRegisteredPersons().contains(person)) perimeter.getRegisteredPersons().remove(person);
    }

    public Perimeter getCurrentPerimeter(Position position) {
        int perimeterI = (int) Math.floor(position.getXValue() / perimeterSize);
        int perimeterJ = (int) Math.floor(position.getYValue() / perimeterSize);
        return perimeters.get(perimeterI).get(perimeterJ);

    }

    public Vector<Perimeter> getAllNodes() {
        Vector<Perimeter> perimetersList = new Vector<>();
        for (int i = 0; i < this.numberOfPerimetersX; i++) {
            for (int j = 0; j < this.numberOfPerimetersY; j++)
			    perimetersList.add(this.perimeters.get(i).get(j));
	    }
        return perimetersList;
    }
}
