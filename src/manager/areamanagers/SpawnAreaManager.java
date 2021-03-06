package manager.areamanagers;

import model.Room;
import model.areas.Area;
import model.areas.SpawnArea;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fluht1 on 09/11/16.
 * Handles the spawn areas
 */
public class SpawnAreaManager extends AreaManager {
    private static SpawnAreaManager instance;
    private List<SpawnArea> spawnAreas = new ArrayList<>();
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

    public List<SpawnArea> getSpawnAreas() {
        return spawnAreas;
    }

    public void add(SpawnArea o) {
        super.add(o);
        this.getSpawnAreas().add(o);
    }

    public void clear() {
        this.spawnAreas.clear();
    }

    @Override
    public void remove(Area a) {
        super.remove(a);
        this.getSpawnAreas().remove(a);
    }
}
