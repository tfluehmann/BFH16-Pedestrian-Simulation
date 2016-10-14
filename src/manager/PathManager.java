package manager;

import model.GVector;
import model.Position;

import java.util.*;

/**
 * Created by tgdflto1 on 06/10/16.
 * <p>
 * findValidEdges = ownWritten
 * <p>
 * dijkstra --> clearly inspired and mostly copied from http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html#shortestpath_graph (08.10.16)
 */
public class PathManager {
    private final List<Position> vertices = new ArrayList<>();
    private final Collection<GVector> obstacleEdges = new ArrayList<>(); // edges that are not possible to cross
    private final List<GVector> edges = new ArrayList<>();
    private Set<Position> settledNodes;
    private Set<Position> unSettledNodes;
    private Map<Position, Position> predecessors;
    private Map<Position, Double> distance;
    private LinkedList<Position> path;
    private Position startPosition;
    private Position target;


    /**
     * Fucking magic just took us 10h of Fluch und Hass
     */
    public void findValidEdges() {
        for (int i = 0; i < this.vertices.size(); i++) {
            Position p = this.vertices.get(i);
            for (int j = i + 1; j < this.vertices.size(); j++) {
//                System.out.println("vertices size: "+ vertices.size() + " j is "+ j + " vertex: " + vertices.get(j));
                GVector v = new GVector(p, this.vertices.get(j));
                boolean isCrossing = this.checkAgainstObstacles(v);
                if (!isCrossing) {
                    v.setStyle("-fx-stroke: yellow;");
//                    System.out.println("" + v);
                    this.edges.add(v);
                    // edges.add(v.otherEdge());
                }
            }
        }
    }

    private boolean checkAgainstObstacles(GVector v) {
        boolean isCrossing = false;
        for (GVector obstacleVector : this.obstacleEdges)
            if (obstacleVector.isCrossedWith(v)) {
                isCrossing = true;
                v.setStyle("-fx-stroke: blue;");
                // System.out.println("crossing");
                break;
            }
        return isCrossing;
    }

    public void start() {
        this.findValidEdges();
        this.findShortestPath();
        this.findPathToTarget();
    }

    /**
     * from here dijkstra as described in class comment
     */
    public void findShortestPath() {
        this.settledNodes = new HashSet<>();
        this.unSettledNodes = new HashSet<>();
        this.distance = new HashMap<>();
        this.predecessors = new HashMap<>();
        this.distance.put(this.startPosition, 0.0);
        this.unSettledNodes.add(this.startPosition);
        while (this.unSettledNodes.size() > 0) {
            //set all to double.max
            Position node = this.getMinimum(this.unSettledNodes);
            this.settledNodes.add(node);
            this.unSettledNodes.remove(node);
            this.findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Position node) {
        List<Position> adjacentNodes = this.getNeighbors(node);
//        System.out.println("neighbor nodes: " + adjacentNodes.size());
        for (Position target : adjacentNodes) {
//            System.out.println("do magic");
            double shortestTOTarget = this.getShortestDistance(target);

            if (shortestTOTarget > this.getShortestDistance(node) + this.getDistance(node, target)) {
                this.distance.put(target, this.getShortestDistance(node) + this.getDistance(node, target));
//                System.out.println("from target: " + target + " to node: " + node + "  dist: " + distance.get(distance.size() - 1));
                this.predecessors.put(target, node);
//                System.out.println("predecessors: " + predecessors.size());
                this.unSettledNodes.add(target);
            }
        }
    }

    private double getDistance(Position node, Position target) {
//        System.out.println("edges: " + edges.size());
        for (GVector edge : this.edges) {
            //  System.out.println("checking distance for "+node+" and "+target + " with edge: "+edge);
            if (edge.getStartPosition().equals(node) && edge.getEndPosition().equals(target) ||
                    edge.getStartPosition().equals(target) && edge.getEndPosition().equals(node)) {
//                System.out.println("found matching edge: " + edge);
                return edge.length();
            }
        }
//        System.out.println("did not find a matching edge");
        throw new RuntimeException("Should not happen, getDistance and getNeighbors probably have different algorithms");
    }

    private List<Position> getNeighbors(Position node) {
        List<Position> neighbors = new ArrayList<>();
        for (GVector edge : this.edges)
            if (edge.getStartPosition().equals(node) && !this.isSettled(edge.getEndPosition())) {
                neighbors.add(edge.getEndPosition());
            } else if (edge.getEndPosition().equals(node) && !this.isSettled(edge.getStartPosition())) {
                neighbors.add(edge.getStartPosition());
            }
        return neighbors;
    }

    private Position getMinimum(Set<Position> vertexes) {
        Position minimum = null;
        for (Position vertex : vertexes) {
            if (minimum == null)
                minimum = vertex;
            else if (this.getShortestDistance(vertex) < this.getShortestDistance(minimum))
                    minimum = vertex;
        }
        return minimum;
    }

    private boolean isSettled(Position vertex) {
        return this.settledNodes.contains(vertex);
    }

    private double getShortestDistance(Position destination) {
        Double d = distance.get(destination);
        if (d == null)
            return Double.MAX_VALUE;
        else
            return d;
    }

    /**
     * last method dijkstra
     */
    public void findPathToTarget() {
        path = new LinkedList<>();
        Position step = this.target;

        // check if a path exists
//        System.out.println("looking for: " + target);
//        System.out.println("got: " + getKey(target));
        if (this.getKey(step) == null) {
            return;
        }
        path.add(step);
        Position lastStep = null;
        while (this.getKey(step) != null && !step.equals(lastStep)) {
//            System.out.println("last_step = " + lastStep + " this step: " + step);
            step = getKey(step);
            path.add(step);
            lastStep = step;
        }
        // Put it into the correct order
        Collections.reverse(path);
        System.out.println("calculated Path: " + path);
    }

    private Position getKey(Position pos) {
        for (Position p : this.predecessors.keySet()) {
            if (pos.equals(p)) return this.predecessors.get(p);
        }
        return null;
    }

    public List<Position> getVertices() {
        return this.vertices;
    }

    public Collection<GVector> getObstacleEdges() {
        for (GVector v : this.obstacleEdges)
            v.setStyle("-fx-stroke: red;");
        return this.obstacleEdges;
    }

    public List<GVector> getEdges() {
        return this.edges;
    }

    public LinkedList<Position> getPath() {
        return path;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public void setTarget(Position target) {
        this.target = target;
    }


}
