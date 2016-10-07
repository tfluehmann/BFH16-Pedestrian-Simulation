package model.areas;

import javafx.scene.paint.Color;
import model.GVector;
import model.Position;
import model.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suter1 on 06.10.2016.
 */
public class Obstacle extends Area {
    public static final double EDGE_EXTENDER = 15.0;
    private Position position;
    private ArrayList<Position> corners;
    private ArrayList<Position> vertices = new ArrayList<>();

    /**
     * Created by suter1 on 06.10.2016.
     */
    public Obstacle(Double... points) {
        this.corners = new ArrayList<>();
        this.vertices = new ArrayList<>();

        for (int i = 0; i < points.length; i += 2) {
            this.corners.add(new Position(points[i], points[i + 1]));
        }
        calculateVertices();

        this.position = new Position(points[0], points[1]);
        this.setFill(Color.BLACK);
        this.getPoints().addAll(points);
    }

    public List<Position> getCorners() {
        return this.corners;
    }

    /**
     * Created by suter1 on 06.10.2016.
     */
    public void calculateVertices() {
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
            /**
             * check if position in room
             */
            if (a.getXValue() > 0 && a.getXValue() < Room.ROOM_WIDTH && a.getYValue() > 0 && a.getYValue() < Room.ROOM_HEIGHT)
                this.vertices.add(a);
            if (b.getXValue() > 0 && b.getXValue() < Room.ROOM_WIDTH && b.getYValue() > 0 && b.getYValue() < Room.ROOM_HEIGHT)
                this.vertices.add(b);
        }
    }

    public List<Position> getVertices() {
        return this.vertices;
    }
}
