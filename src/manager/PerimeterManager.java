package manager;

import config.ConfigModel;
import model.Perimeter;
import model.Position;
import model.persons.Person;

import java.util.ArrayList;
import java.util.List;
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
        if (perimeter == null) setInitialPerimeter();
        for (Perimeter neigh : perimeter.getNeighbors()) {
            if (neigh.isInRange(person.getCurrentPosition()))
                neigh.register(person);
        }

        if (person.getCurrentPosition() == null)
            throw new RuntimeException("No Perimeter for Person found " + person.getCurrentPosition());
    }

    private void setInitialPerimeter() {

    }

    public Vector<Perimeter> getNeighbors(Position position) {
        Perimeter perimeter = getCurrentPerimeter(position);
        int i = perimeter.getHorizontalArrayPosition() - 1;
        int j = perimeter.getVerticalArrayPosition() - 1;
        Vector<Perimeter> neighbors = new Vector<>();
	    for (int a = i; a <= i + 2; a++)
		    for (int b = j; b <= j + 2; b++)
                if (i >= 0 && i < this.perimeters.size() && j >= 0 && j < this.perimeters.get(i).size())
                    neighbors.add(this.perimeters.get(i).get(j));
	    return neighbors;
    }

    public void unregisterPerson(Person person, Perimeter perimeter) {
        if (perimeter.getRegisteredPersons().contains(person)) perimeter.getRegisteredPersons().remove(person);
    }

    public void movePersonRegistration(Person person) {
        Perimeter currentPerimeter = getCurrentPerimeter(person.getCurrentPosition());
        this.registerPerson(person);
        this.unregisterPerson(person, currentPerimeter);
    }

    public Perimeter getCurrentPerimeter(Position position) {
        int perimeterI = ((int) Math.floor(position.getXValue())) % (int) numberOfPerimetersX;
        int perimeterJ = ((int) Math.floor(position.getXValue())) % (int) numberOfPerimetersY;
        return perimeters.get(perimeterI).get(perimeterJ);
    }

    public List<Perimeter> getAllNodes() {
        List<Perimeter> perimetersList = new ArrayList<>();
	    for (int i = 0; i < this.numberOfPerimetersX; i++) {
		    for (int j = 0; j < this.numberOfPerimetersY; j++)
			    perimetersList.add(this.perimeters.get(i).get(j));
	    }
        return perimetersList;
    }
}
