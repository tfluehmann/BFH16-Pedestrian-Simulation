package manager.areamanagers;

import manager.SpawnManager;
import model.GVector;
import model.Room;
import model.areas.Obstacle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgdflto1 on 28/10/16.
 */
public class ObstacleManager extends AreaManager {
    private static ObstacleManager instance;
    private List<Obstacle> obstacles = new ArrayList<>();
    private Room simulationRoom;

    public void setRoom(Room room) {
        simulationRoom = room;
    }

    public Room getRoom() {
        return simulationRoom;
    }
    private ObstacleManager() {
    }

    public static ObstacleManager getInstance() {
        if (instance == null) instance = new ObstacleManager();
        return instance;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void add(Obstacle o) {
        super.add(o);
        this.getObstacles().add(o);
    }

    public boolean isCrossingAnyObstacle(GVector v) {
        boolean isCrossing = false;
        for (GVector obstacleVector : SpawnManager.getInstance().getPathManager().getObstacleEdges())
            if (v.isCrossedWith(obstacleVector)) {
                isCrossing = true;
                break;
            }
        return isCrossing;
    }
}