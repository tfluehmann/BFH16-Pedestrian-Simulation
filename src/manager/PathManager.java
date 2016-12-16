package manager;

import manager.areamanagers.ObstacleManager;
import model.*;

import java.util.*;

/**
 * Created by tgdflto1 on 06/10/16.
 * <p>
 * <p>
 */
public class PathManager {
    private final List<Vertex> vertexList = new ArrayList<>();
    private final List<GVector> obstacleEdges = new ArrayList<>(); // edges that are not possible to cross
    private final Set<Vertex> settledNodes = new HashSet<>();
    private Map<Vertex, Double> nodes = new HashMap<>();
    private List<TargetVertex> targetVertices = new ArrayList<>();

    public PathManager(TargetVertex... targetVertices) {
        this.targetVertices.addAll(Arrays.asList(targetVertices));
        this.vertexList.addAll(this.targetVertices);
    }

    /**
     * Fucking magic just took us 10h of Fluch und Hass
     * we add neighbors in both directions, we do not have to check the ones already done
     * <p>
     * a b c d
     * a x - - -
     * b x x - -
     * c x x x -
     * d x x x x
     **/
    public void findValidEdges(Room room) {
        for (int i = 0; i < this.vertexList.size(); i++) {
            Vertex vertex = vertexList.get(i);
            for (int j = i + 1; j < this.vertexList.size(); j++) {
                Vertex targetVertex = this.vertexList.get(j);
                if (vertex.equals(targetVertex)) continue;
                GVector v = new GVector(vertex.getPosition(), targetVertex.getPosition());
                boolean isCrossing = ObstacleManager.getInstance().isCrossingAnyObstacle(v);
                if (!isCrossing) {
                    v.setStyle("-fx-stroke: green;");
                    room.getChildren().add(v);
                    double distance = v.length();
                    if (nodes.containsKey(targetVertex)) {
                        if (distance < nodes.get(targetVertex)) nodes.put(targetVertex, distance);
                    } else
                        nodes.put(targetVertex, distance);
                    vertex.addNeighbor(targetVertex, distance);
                    targetVertex.addNeighbor(vertex, distance);
                }
            }
        }
    }

    public void calculatePaths() {
        for (TargetVertex target : targetVertices)
	    dijkstra(target);
    }

    private void dijkstra(TargetVertex target) {
        afterDijkstraCleanup();
        Queue<Node> pq = new PriorityQueue<>(new Node());//Heap to extract value
        Map<Vertex, Double> distances = new HashMap<>();
        for (Vertex v : vertexList)
            distances.put(v, Double.MAX_VALUE);
        distances.put(target, 0.0);
        pq.clear();
        pq.offer(new Node(target, 0));

        while (!pq.isEmpty()) {
            Node element = pq.poll();
            Vertex candidate = element.getVertex();//Getting next node from heap
            double cost = element.getWeight();
            candidate.setVisited(target, true);
            settledNodes.add(candidate);
            for (Vertex z : candidate.getNeighbors().keySet()) {
                if (candidate.equals(z)) continue;
                GVector vector = new GVector(z.getPosition(), candidate.getPosition());
                double newDist = cost + vector.length();
                if (distances.get(z) > newDist) { //Checking for min weight
                    z.setTarget(target, candidate, newDist);
                    distances.put(z, newDist);
                    pq.offer(new Node(z, distances.get(z)));//Adding element to PriorityQueue
                }
            }
        }
    }

    private void afterDijkstraCleanup() {
        settledNodes.clear();
        nodes.clear();
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    /**
     * @param currentPosition
     *
     * @return
     */
    public Vertex getClosestVertex(Position currentPosition) {
        Vertex possibleVertex = null;
        for (TargetVertex targetVertex : targetVertices) {
            double possibleDist = 0;
            for (Vertex vertex : getVertexList()) {
                GVector vect = new GVector(vertex.getPosition(), currentPosition);
                if (ObstacleManager.getInstance().isCrossingAnyObstacle(vect)) continue;
                if (possibleVertex == null) {
                    possibleVertex = vertex;
                    possibleDist = possibleVertex.getDistance(targetVertex);
                } else if (possibleDist > vertex.getDistance(targetVertex))
                    possibleVertex = vertex;
            }
        }
        if (possibleVertex == null) System.out.println("possible vertex null");
        return possibleVertex;
    }

    public Collection<GVector> getObstacleEdges() {
        return obstacleEdges;
    }

    public List<TargetVertex> getTargetVertices() {
        return this.targetVertices;
    }

    public void addTarget(TargetVertex target) {
        this.targetVertices.add(target);
        this.vertexList.add(target);
    }

    public void clear() {
        vertexList.clear();
        obstacleEdges.clear();
        targetVertices.clear();
        afterDijkstraCleanup();
    }
}
