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
    private List<Position> vertices = new ArrayList<>();
    private List<GVector> obstacleEdges = new ArrayList<>(); // edges that are not possible to cross
    private List<GVector> edges = new ArrayList<>();

    private Set<Position> settledNodes;
    private Set<Position> unSettledNodes;
    private Map<Position, Position> predecessors;
    private Map<Position, Double> distance;

    private static PathManager instance;

    private PathManager() {
    }

    public void findValidEdgesAndSetWeigth() {
        for (int i = 0; i < vertices.size(); i++) {
            Position p = vertices.get(i);
            for (int j = i + 1; j < vertices.size(); j++) {
                GVector v = new GVector(p, vertices.get(j));
                boolean isCrossing = false;
                for (GVector obstacleVector : obstacleEdges)
                    if (obstacleVector.isCrossedWith(v)) {
                        isCrossing = true;
                        break;
                    }
                if (!isCrossing) {
                    edges.add(v);
                }
            }
        }
    }

    /**
     * from here dijkstra as described in class comment
     *
     * @param startPosition
     *
     * @return
     */
    public void findShortestPath(Position startPosition) {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        distance.put(startPosition, 0.0);
        unSettledNodes.add(startPosition);
        while (unSettledNodes.size() > 0) {
            Position node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Position node) {
        List<Position> adjacentNodes = getNeighbors(node);
        for (Position target : adjacentNodes)
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
    }

    private double getDistance(Position node, Position target) {
        for (GVector edge : edges) {
            if (edge.getStartPosition().equals(node)
                    && edge.getEndPosition().equals(target)) {
                return edge.length();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Position> getNeighbors(Position node) {
        List<Position> neighbors = new ArrayList<>();
        for (GVector edge : edges)
            if (edge.getStartPosition().equals(node)
                    && !isSettled(edge.getEndPosition())) {
                neighbors.add(edge.getEndPosition());
            }
        return neighbors;
    }

    private Position getMinimum(Set<Position> vertexes) {
        Position minimum = null;
        for (Position vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Position vertex) {
        return settledNodes.contains(vertex);
    }

    private double getShortestDistance(Position destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Double.MAX_VALUE;
        } else {
            return d;
        }
    }

    /**
     * last method dijkstra
     *
     * @param target
     *
     * @return
     */
    public LinkedList<Position> getPath(Position target) {
        LinkedList<Position> path = new LinkedList<>();
        Position step = target;
        // check if a path exists
        System.out.println("predecessors: " + predecessors.size() + " step to target " + step);
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }


    public static PathManager getInstance() {
        if (instance == null) instance = new PathManager();
        return instance;
    }

    public List<Position> getVertices() {
        return vertices;
    }

    public List<GVector> getObstacleEdges() {
        return obstacleEdges;
    }


//    List<Integer> edges = new EdgeList
}
