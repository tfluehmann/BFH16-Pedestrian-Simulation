package manager.areamanagers;

import model.Anchor;
import model.Room;
import model.areas.Area;

import java.util.List;

/**
 * Created by tgdflto1 on 09/11/16.
 * Abstract class that handles the simulation room and its areas
 */
public abstract class AreaManager {
    private Room simulationRoom;

    public void add(Area a) {
        if (!getRoom().getChildren().containsAll(a.getAnchors())) getRoom().getChildren().addAll(a.getAnchors());
        getRoom().getChildren().add(a);
    }

    public void addAnchors(List<Anchor> anchors) {
        getRoom().getChildren().addAll(anchors);
    }

    public void removeAnchors(List<Anchor> anchors) {
        for (Anchor anchor : anchors) {
            getRoom().getChildren().removeIf(item -> (item.equals(anchor)));
        }
    }

    public void setRoom(Room room) {
        simulationRoom = room;
    }

    public Room getRoom() {
        return simulationRoom;
    }

    public abstract void clear();

	public void remove(Area a){
        getRoom().getChildren().removeAll(a.getAnchors());
        getRoom().getChildren().remove(a);
    }
}
