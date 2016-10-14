package manager;

import config.ConfigModel;
import model.Perimeter;
import model.Position;
import model.Room;
import model.persons.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by tgdflto1 on 05/10/16.
 */
public class PerimeterManager {
    private static PerimeterManager instance;
    private Room room;
    private Vector<Vector<Perimeter>> perimeters;
    private long numberOfPerimetersY;
    private long numberOfPerimetersX;


    private PerimeterManager() {
    }


    public static PerimeterManager getInstance(){
        if(instance == null)
            instance = new PerimeterManager();
        return instance;
    }


    public void initializeAll() {
        ConfigModel config = ConfigModel.getInstance();
        Double height = config.getRoomHeight();
        Double width = config.getRoomWidth();

        double perimeterSize = config.getPersonRadius() * 10;
        numberOfPerimetersY = Math.round(height.intValue() / perimeterSize);
        numberOfPerimetersX = Math.round(width.intValue() / perimeterSize);
        perimeters = new Vector<>();
        for (int i = 0; i < numberOfPerimetersX; i++) {
            Vector<Perimeter> currentList = new Vector<>();
            for (int j = 0; j < numberOfPerimetersY; j++) {
                currentList.add(new Perimeter(new Position(i * perimeterSize, j * perimeterSize),
                        perimeterSize, perimeterSize, i, j));
            }
            perimeters.add(currentList);
        }
    }

    public void registerPerson(Person person){
        for(int i = 0; i < numberOfPerimetersX; i++)
            for(int j = 0; j < numberOfPerimetersY; j++)
                if (perimeters.get(i).get(j).isInRange(person.getCurrentPosition()))
                    perimeters.get(i).get(j).register(person);
        if (person.getCurrentPerimeter() == null)
            throw new RuntimeException("No Perimeter for Person found " + person.getCurrentPosition());
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Vector<Perimeter> getNeighbors(Perimeter perimeter) {
        int i = perimeter.getHorizontalArrayPosition() - 1;
        int j = perimeter.getVerticalArrayPosition() - 1;
        Vector<Perimeter> neighbors = new Vector<>();
        for(int a = i; a <= (i +2); a++)
            for(int b = j; b <= (j + 2); b++)
                if (i > 0 && i < perimeters.size())
                    if (j > 0 && j < perimeters.get(i).size())
                        neighbors.add(perimeters.get(i).get(j));
        return neighbors;
    }

    public void unregisterPerson(Person person){
        Perimeter p = person.getCurrentPerimeter();
        p.getRegistredPersons().remove(person);
        person.setCurrentPerimeter(null);
    }

    public void movePersonRegistration(Person person) {
        System.out.println("reregister");
        unregisterPerson(person);
        registerPerson(person);
    }

    public List<Perimeter> getAllNodes() {
        List<Perimeter> perimetersList = new ArrayList<>();
        for (int i = 0; i < numberOfPerimetersX; i++) {
            for (int j = 0; j < numberOfPerimetersY; j++)
                perimetersList.add(perimeters.get(i).get(j));
        }
        return perimetersList;
    }
}
