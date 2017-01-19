package model.areas;

import manager.areamanagers.ObstacleManager;
import model.ConfigModel;
import model.GVector;
import model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suter1 on 06.10.2016.
 * An obstacle that cannot be passed from a person
 */
public class Obstacle extends Area {
    private final ArrayList<Position> corners;
    private ArrayList<Position> edgePoints = new ArrayList<>();

    /**
     * Created by suter1 on 06.10.2016.
     * @param points as array
     */
    public Obstacle(double... points) {
        super(ObstacleManager.getInstance(), points);
        corners = new ArrayList<>();
        edgePoints = new ArrayList<>();
        getStyleClass().add("obstacle");
    }

    public void calculateCornersAndVertices() {
        corners.clear();
        for (int i = 0; i < getPoints().size(); i += 2) {
            Position pos = new Position(getPoints().get(i), getPoints().get(i + 1));
            this.corners.add(pos);
        }
        this.calculateVertices();
    }

    /**
     * Created by suter1 on 06.10.2016.
     */
    private void calculateVertices() {
        double edgeExtender = ConfigModel.getInstance().getEdgeExtender();
        edgePoints.clear();
        Position a, b;
        for (int i = 0; i < this.corners.size(); i++) {
            // once around the obstacle, last is first
            b = (i + 1 >= this.corners.size()) ? this.corners.get(0) : this.corners.get(i + 1);
            a = this.corners.get(i);
            GVector c = new GVector(a.getXValue(), a.getYValue(), b.getXValue(), b.getYValue());
            GVector unitVector = c.norm();
            a = unitVector.invert().getEndPosition().multiply(edgeExtender).add(a);
            b = unitVector.getEndPosition().multiply(edgeExtender).add(b);
            if (this.includes(a) && !this.contains(a.getXValue(), a.getYValue())) this.edgePoints.add(a);
            if (this.includes(b) && !this.contains(b.getXValue(), b.getYValue())) this.edgePoints.add(b);
        }
    }

    /**
     * check if position in room
     */
    private boolean includes(Position position) {
        ConfigModel config = ConfigModel.getInstance();
        return position.getXValue() > 0 &&
                position.getXValue() < config.getRoomWidth() &&
                position.getYValue() > 0 &&
                position.getYValue() < config.getRoomHeight();
    }

    @Override
    public List<Position> getCorners() {
        return this.corners;
    }

    public ArrayList<Position> getEdgePoints() {
        return edgePoints;
    }
}
