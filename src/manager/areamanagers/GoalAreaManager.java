package manager.areamanagers;

import model.Room;
import model.areas.GoalArea;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgdflto1 on 09/11/16.
 */
public class GoalAreaManager extends AreaManager {
    private static GoalAreaManager instance;
    private List<GoalArea> goalAreas = new ArrayList<>();
    private Room simulationRoom;

    public void setRoom(Room room) {
        simulationRoom = room;
    }

    public Room getRoom() {
        return simulationRoom;
    }

    private GoalAreaManager() {
    }

    public static GoalAreaManager getInstance() {
        if (instance == null) instance = new GoalAreaManager();
        return instance;
    }

    public List<GoalArea> getObstacles() {
        return goalAreas;
    }

    public void add(GoalArea o) {
        this.getObstacles().add(o);
        getRoom().getChildren().add(o);
    }


}
