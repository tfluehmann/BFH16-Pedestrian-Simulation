package model.areas;

import javafx.scene.paint.Color;
import model.Position;
import model.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suter1 on 06.10.2016.
 */
public class Obstacle extends Area {
    public static final double EDGE_EXTENDER = 5.0;
    private Position position;
    private ArrayList<Position> edges;
    private ArrayList<Position> extendedEdges = new ArrayList<>();

    /**
     * Created by suter1 on 06.10.2016.
     */
    public Obstacle(Double... points) {
        this.edges = new ArrayList<>();
        this.extendedEdges = new ArrayList<>();

        for (int i = 0; i < points.length; i += 2) {
            this.edges.add(new Position(points[i], points[i + 1]));
        }
        this.position = new Position(points[0], points[1]);
        this.setFill(Color.BLACK);
        this.getPoints().addAll(points);
    }

    @Override
    public List<Position> getEdges() {
        return this.edges;
    }

    /**
     * Created by suter1 on 06.10.2016.
     */
    public void setExtendedEdges() {
        Position a, b;
        double aX, aY, bX, bY;

        for (int i = 0; i < this.edges.size(); i++) {
            if (i + 1 >= this.edges.size()) {
                b = this.edges.get(0);
            } else {
                b = this.edges.get(i + 1);
            }
            a = this.edges.get(i);

            Position c = new Position(b.getXValue() - a.getXValue(), b.getYValue() - a.getYValue());
            double lengthC = Math.sqrt(Math.pow(c.getXValue(), 2) + Math.pow(c.getYValue(), 2));
            Position unitVector = new Position(c.getXValue() / lengthC, c.getYValue() / lengthC);

            if (a.getXValue() > b.getXValue()) {
                aX = a.getXValue() + EDGE_EXTENDER * unitVector.getXValue();
                bX = b.getXValue() - EDGE_EXTENDER * unitVector.getXValue();
            } else {
                aX = a.getXValue() - EDGE_EXTENDER * unitVector.getXValue();
                bX = b.getXValue() + EDGE_EXTENDER * unitVector.getXValue();
            }
            if (a.getYValue() > b.getYValue()) {
                aY = a.getYValue() + EDGE_EXTENDER * unitVector.getYValue();
                bY = b.getYValue() - EDGE_EXTENDER * unitVector.getYValue();
            } else {
                aY = a.getYValue() - EDGE_EXTENDER * unitVector.getYValue();
                bY = b.getYValue() + EDGE_EXTENDER * unitVector.getYValue();
            }

            if (aX > 0 && aX < Room.ROOM_WIDTH && aY > 0 && aY < Room.ROOM_HEIGHT)
                this.extendedEdges.add(new Position(aX, aY));
            if (bX > 0 && bX < Room.ROOM_WIDTH && bY > 0 && bY < Room.ROOM_HEIGHT)
                this.extendedEdges.add(new Position(bX, bY));
        }
    }

    public List<Position> getExtendetEdges() {
        return this.extendedEdges;
    }
}
