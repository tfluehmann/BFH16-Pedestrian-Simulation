package model.areas;

import model.ConfigModel;
import model.GVector;
import model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suter1 on 06.10.2016.
 */
public class Obstacle extends Area {
    private final ArrayList<Position> corners;
    private ArrayList<Position> edgePoints = new ArrayList<>();

    /**
     * Created by suter1 on 06.10.2016.
     */
    public Obstacle(double... points) {
        super(points);
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
    public boolean includes(Position position) {
        ConfigModel config = ConfigModel.getInstance();
        System.out.println(position);
        System.out.println(config.getRoomWidth());
        System.out.println(config.getRoomHeight());
        System.out.println();
        return position.getXValue() > 0 &&
                position.getXValue() < config.getRoomWidth() &&
                position.getYValue() > 0 &&
                position.getYValue() < config.getRoomHeight();
    }

    public List<Position> getEdgePoints() {
        return this.edgePoints;
    }

    @Override
    public List<Position> getCorners() {
        return this.corners;
    }

}
