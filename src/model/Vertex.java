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
    private Map<TargetVertex, Double> distanceToTarget = new HashMap<>();
    private Map<TargetVertex, Boolean> visitedForTarget = new HashMap<>();

    public Vertex(Position position) {
        neighbors = new HashMap<>();
        this.position = position;
    }

    public Vertex getNextHopForTarget(TargetVertex target) {
        return nextHopsToTarget.get(target);
    }

    public Double getDistance(TargetVertex target) {
        //in case there are no obstacles, set target is never called
        if (distanceToTarget.get(target) == null) {
            return new GVector(this.position, target.getPosition()).length();
        } else {
            return distanceToTarget.get(target);
        }
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
        nextHopsToTarget.put(target, nextHop);
        distanceToTarget.put(target, newDist);
    }

    public void setVisited(TargetVertex targetVertex, boolean visited) {
        visitedForTarget.put(targetVertex, visited);
    }

    public boolean isVisited(TargetVertex target) {
        return visitedForTarget.get(target);
    }

    public TargetVertex getShortestTarget() {
        TargetVertex shortestTarget = null;
        double shortestDistance = 0.0;
        for (TargetVertex candidateTarget : nextHopsToTarget.keySet()) {
            if (shortestTarget == null) {
                shortestTarget = candidateTarget;
                shortestDistance = distanceToTarget.get(candidateTarget);
            } else if (shortestDistance > distanceToTarget.get(candidateTarget)) {
                shortestDistance = distanceToTarget.get(candidateTarget);
                shortestTarget = candidateTarget;
            }
        }
        return shortestTarget;
    }

}


