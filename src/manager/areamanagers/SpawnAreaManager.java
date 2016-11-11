package manager.areamanagers;

import model.Room;
import model.areas.SpawnArea;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgdflto1 on 09/11/16.
 */
public class SpawnAreaManager extends AreaManager {
    private static SpawnAreaManager instance;
    private List<SpawnArea> obstacles = new ArrayList<>();
    private Room simulationRoom;

    public void setRoom(Room room) {
        simulationRoom = room;
    }

    public Room getRoom() {
        return simulationRoom;
    }

    private SpawnAreaManager() {
    }

    public static SpawnAreaManager getInstance() {
        if (instance == null) instance = new SpawnAreaManager();
        return instance;
    }

    public List<SpawnArea> getObstacles() {
        return obstacles;
    }

    public void add(SpawnArea o) {
        this.getObstacles().add(o);
        getRoom().getChildren().add(o);
    }
}
