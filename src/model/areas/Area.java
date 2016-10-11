package model.areas;

import javafx.scene.shape.Polygon;
import model.GVector;
import model.Position;
import model.Positionable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class  Area extends Polygon implements Positionable{
    protected Position position;
    protected Set<GVector> edges = new HashSet<>();

    public abstract List<Position> getCorners();


    @Override
    public Position getCurrentPosition() {
        return position;
    }

    public Area(double... points) {
        super(points);
        System.out.println("points: " + getPoints().size());
        calculateEdges();
        System.out.println("edges calculated (" + edges.size() + ")");
    }

    @Override
    public boolean intersects(double x, double y) {
        return this.contains(x, y);
    }

    /**
     * from each point to next point-> create gvector --> edge
     */
    public void calculateEdges() {
        System.out.println(getPoints());
        for (int x = 0; x < getPoints().size(); x += 2) {
            int y = x + 1;
            int x1 = y + 1;
            if (x1 == getPoints().size()) x1 = 0;
            int y1 = x1 + 1;
            Position start = new Position(this.getPoints().get(x), this.getPoints().get(y));
            Position end = new Position(this.getPoints().get(x1), this.getPoints().get(y1));
            GVector edge = new GVector(start, end);
            this.edges.add(edge);
        }
    }

    public Set<GVector> getEdges() {
        return edges;
    }
}
