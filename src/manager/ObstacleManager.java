package manager;

import model.areas.Obstacle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgdflto1 on 28/10/16.
 */
public class ObstacleManager {
    private static ObstacleManager instance;
    private List<Obstacle> obstacles = new ArrayList<>();

    private ObstacleManager() {
    }

    public static ObstacleManager getInstance() {
        if (instance == null) instance = new ObstacleManager();
        return instance;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }
}
