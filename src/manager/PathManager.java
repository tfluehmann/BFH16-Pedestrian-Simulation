package manager;

import model.GVector;
import model.Position;
import model.Room;
import model.Vertex;

import java.util.*;

/**
 * Created by tgdflto1 on 06/10/16.
 * <p>
 * <p>
 */
public class PathManager {
    private final List<Vertex> vertexList = new ArrayList<>();
    private final Collection<GVector> obstacleEdges = new ArrayList<>(); // edges that are not possible to cross
    private final Map<Vertex, Vertex> settledNodes = new HashMap<>();

    /**
     * Fucking magic just took us 10h of Fluch und Hass
     * we add neighbors in both directions, we do not have to check the ones already done
     *
     * a b c d
     * a x - - -
     * b x x - -
     * c x x x -
     * d x x x x
     *
     **/
    public void findValidEdges(Room room) {
        for (int i = 0; i < this.vertexList.size(); i++) {
            Vertex vertex = vertexList.get(i);
            for (int j = i + 1; j < this.vertexList.size(); j++) {
                Vertex targetVertex = this.vertexList.get(j);
                if (vertex.equals(targetVertex)) continue;
                GVector v = new GVector(vertex.getPosition(), targetVertex.getPosition());
                boolean isCrossing = this.checkAgainstObstacles(v);
                if (!isCrossing) {
                    v.setStyle("-fx-stroke: green;");
                    room.getChildren().add(v);
                    double distance = v.length();
                    vertex.addNeighbor(targetVertex, distance);
                    targetVertex.addNeighbor(vertex, distance);
                }
            }
        }
    }

    private boolean checkAgainstObstacles(GVector v) {
        boolean isCrossing = false;
        for (GVector obstacleVector : this.obstacleEdges)
            if (v.isCrossedWith(obstacleVector)) {
                isCrossing = true;
                break;
            }
        return isCrossing;
    }

    public void findShortestPath(Vertex target) {
        findMinimumDistance(target, null, 0.0);
    }

    private void findMinimumDistance(Vertex nextVertex, Vertex lastVertex, double currentLength) {
        if (nextVertex == null) return;
        settledNodes.put(nextVertex, lastVertex);

        Vertex shortestNeighbor = null;
        for (Vertex neighbor : nextVertex.getNeighbors().keySet()) {
            if (shortestNeighbor == null && !settledNodes.containsKey(neighbor)) shortestNeighbor = neighbor;

            if (shortestNeighbor != null && nextVertex.getNeighbors().get(neighbor) < nextVertex.getNeighbors().get(shortestNeighbor) &&
                    !settledNodes.containsKey(neighbor)) shortestNeighbor = neighbor;
        }
        if (shortestNeighbor == null) return;
        findMinimumDistance(shortestNeighbor, nextVertex, currentLength + nextVertex.getNeighbors().get(shortestNeighbor));
    }

    private LinkedList<Vertex> getPath(Vertex start, LinkedList<Vertex> currentPath) {
        Vertex nextVertex = settledNodes.get(start);
        if (nextVertex == null) return currentPath;
        currentPath.add(nextVertex);
        // the list is passed by reference, it is always the same list and we just fill it until done
        return getPath(nextVertex, currentPath);
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public LinkedList<Vertex> getShortestPathFromPosition(Position currentPosition) {
        LinkedList<Vertex> targetList = new LinkedList<>();
        targetList.add(vertexList.get(vertexList.size() - 1));
        Vertex shortestVector = null;
        double shortestDistance = Double.MAX_VALUE;
        GVector goalVector = new GVector(currentPosition, vertexList.get(vertexList.size() - 1).getPosition());
        if (!checkAgainstObstacles(goalVector)) return targetList;
        for (Vertex v : vertexList) {
            GVector vect = new GVector(v.getPosition(), currentPosition);
            if (vect.length() < shortestDistance && !checkAgainstObstacles(vect)) {
                shortestDistance = vect.length();
                shortestVector = v;
            }
        }

        return getPath(shortestVector, new LinkedList<>());
    }

    public Collection<GVector> getObstacleEdges() {
        return obstacleEdges;
    }
}
