package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by tgdflto1 on 19/10/16.
 * represents a position in a graph
 */
public class Vertex extends Observable {
    private Position position;
    private Map<Vertex, Double> neighbors = new HashMap<>();
    //Target -> nexthop
    private Map<TargetVertex, Vertex> nextHopsToTarget = new HashMap<>();
    //nexthop -> distance
    private Map<Vertex, Double> nextHopDistance = new HashMap<>();
    private boolean isVisited = false;
    private double distanceToTarget;

    public Vertex(Position position) {
        neighbors = new HashMap<>();
        this.position = position;
    }

    public Vertex getNextHopForTarget(Vertex target) {
        return nextHopsToTarget.get(target);
    }

    public Double getDistance(Vertex target) {
        return nextHopDistance.get(nextHopsToTarget.get(target));
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

    public void setTarget(TargetVertex target, Vertex nextHop, double newDist) {
        System.out.println("added targetvertex " + target);
        nextHopsToTarget.put(target, nextHop);
        nextHopDistance.put(nextHop, newDist);
        for (double distToNextVertex : nextHopDistance.values())
            distanceToTarget += distToNextVertex;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public double distanceToTarget() {
        return distanceToTarget;
    }

    public TargetVertex getShortestTarget() {
        TargetVertex shortestTarget = null;
        double shortestDistance = 0.0;
        System.out.println("Targets available: " + nextHopsToTarget.keySet().size());
        for (TargetVertex candidateTarget : nextHopsToTarget.keySet()) {
            Vertex nextHop = nextHopsToTarget.get(candidateTarget);
            double candidateDistance = nextHopDistance.get(nextHop);
            if (shortestTarget == null) {
                shortestTarget = candidateTarget;
                shortestDistance = candidateDistance;
                continue;
            }

            if (shortestDistance >= candidateDistance) {
                shortestTarget = candidateTarget;
                shortestDistance = candidateDistance;
            }
        }
        return shortestTarget;
    }

}


