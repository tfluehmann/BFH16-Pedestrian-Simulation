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
    private static final double EDGE_EXTENDER = 20.0;
    private final ArrayList<Position> corners;
    private ArrayList<Position> edgePoints = new ArrayList<>();

    /**
     * Created by suter1 on 06.10.2016.
     */
    public Obstacle(double... points) {
        super(points);
        corners = new ArrayList<>();
        edgePoints = new ArrayList<>();

        for (int i = 0; i < points.length; i += 2) {
            corners.add(new Position(points[i], points[i + 1]));
        }
        this.calculateVertices();

        position = new Position(points[0], points[1]);
        getStyleClass().add("obstacle");
    }

    /**
     * Created by suter1 on 06.10.2016.
     */
    private void calculateVertices() {
        Position a, b;
        for (int i = 0; i < this.corners.size(); i++) {
            if (i + 1 >= this.corners.size()) {
                b = this.corners.get(0);
            } else {
                b = this.corners.get(i + 1);
            }
            a = this.corners.get(i);
            GVector c = new GVector(a.getXValue(), a.getYValue(), b.getXValue(), b.getYValue());
            GVector unitVector = c.norm();
            a = unitVector.invert().getEndPosition().multiply(EDGE_EXTENDER).add(a);
            b = unitVector.getEndPosition().multiply(EDGE_EXTENDER).add(b);

            if (this.includes(a) && !this.contains(a.getXValue(), a.getYValue())) this.edgePoints.add(a);
            if (this.includes(b) && !this.contains(b.getXValue(), b.getYValue())) this.edgePoints.add(b);
        }
    }

    /**
     * check if position in room
     */
    public boolean includes(Position position) {
        ConfigModel config = ConfigModel.getInstance();
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
