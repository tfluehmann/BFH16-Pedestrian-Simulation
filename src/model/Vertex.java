package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tgdflto1 on 19/10/16.
 */
public class Vertex {
    private Position position;
    private Map<Vertex, Double> neighbors = new HashMap<>();

    public Vertex(Position position) {
        neighbors = new HashMap<>();
        this.position = position;
    }

    public void addNeighbor(Vertex v, double distance) {
        this.neighbors.put(v, distance);
    }

    public Position getPosition() {
        return position;
    }

    public Map<Vertex, Double> getNeighbors() {
        return neighbors;
    }
}
