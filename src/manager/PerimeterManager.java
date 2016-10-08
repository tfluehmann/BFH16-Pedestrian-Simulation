package manager;

import config.ConfigModel;
import model.Perimeter;
import model.Position;
import model.Room;
import model.persons.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgdflto1 on 05/10/16.
 */
public class PerimeterManager {
    private static PerimeterManager instance;
    private Room room;
    private Perimeter[][] perimeters;
    private int numberOfPerimetersY;
    private int  numberOfPerimetersX;


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

        numberOfPerimetersY = height.intValue() / Perimeter.PERIMETER_HEIGHT;
        numberOfPerimetersX = width.intValue() / Perimeter.PERIMETER_WIDHT;
        perimeters = new Perimeter[numberOfPerimetersX][numberOfPerimetersY];
        for(int i = 0; i < numberOfPerimetersX; i++)
            for(int j = 0; j < numberOfPerimetersY; j++){
                perimeters[i][j] = new Perimeter(new Position(i * Perimeter.PERIMETER_WIDHT, j * Perimeter.PERIMETER_HEIGHT),
                        Perimeter.PERIMETER_HEIGHT, Perimeter.PERIMETER_WIDHT, i, j);
            }
    }

    public void registerPerson(Person person){
        for(int i = 0; i < numberOfPerimetersX; i++)
            for(int j = 0; j < numberOfPerimetersY; j++)
                if(perimeters[i][j].isInRange(person.getCurrentPosition())) perimeters[i][j].register(person);
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Perimeter> getNeighbors(Perimeter perimeter) {
        int i = perimeter.getHorizontalArrayPosition() - 1;
        int j = perimeter.getVerticalArrayPosition() - 1;
        List<Perimeter> neighbors = new ArrayList<>();
        for(int a = i; a <= (i +2); a++)
            for(int b = j; b <= (j + 2); b++)
                if(i > 0 && i < perimeters.length)
                    if(j > 0 && j < perimeters[i].length)
                        neighbors.add(perimeters[i][j]);
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
}
