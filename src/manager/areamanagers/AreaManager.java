package manager.areamanagers;

import model.Room;
import model.areas.Area;

/**
 * Created by tgdflto1 on 09/11/16.
 */
public abstract class AreaManager {
    protected Room simulationRoom;

    public void add(Area a) {
        getRoom().getChildren().addAll(a.getAnchors());

        getRoom().getChildren().add(a);

    }


    public void setRoom(Room room) {
        simulationRoom = room;
    }

    public Room getRoom() {
        return simulationRoom;
    }
}
