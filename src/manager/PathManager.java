package manager;

import model.*;

import java.util.*;

/**
 * Created by tgdflto1 on 06/10/16.
 * <p>
 * <p>
 */
public class PathManager {
    private final List<Vertex> vertexList = new ArrayList<>();
    private final Collection<GVector> obstacleEdges = new ArrayList<>(); // edges that are not possible to cross
    private final Set<Vertex> settledNodes = new HashSet<>();
    private Map<Vertex, Double> nodes = new HashMap<>();
    private List<Vertex> targetVertexes = new ArrayList<>();

    public PathManager(Vertex... targetVertexes) {
        this.targetVertexes.addAll(Arrays.asList(targetVertexes));
        this.vertexList.addAll(this.targetVertexes);
    }

    /**
     * Fucking magic just took us 10h of Fluch und Hass
     * we add neighbors in both directions, we do not have to check the ones already done
     *
     *   a b c d
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

    private boolean checkAgainstObstacles(GVector v) {
        boolean isCrossing = false;
        for (GVector obstacleVector : this.obstacleEdges)
            if (v.isCrossedWith(obstacleVector)) {
                isCrossing = true;
                break;
            }
        return isCrossing;
    }

    public void crapFindAlgorithm(Vertex target) {
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
            candidate.setVisited(true);
            settledNodes.add(candidate);
            for (Vertex z : candidate.getNeighbors().keySet()) {
                GVector vector = new GVector(z.getPosition(), candidate.getPosition());
                double newDist = cost + vector.length();
                if (distances.get(z) > newDist) {                        //Checking for min weight
                    z.setTarget(target, candidate, newDist);
                    distances.put(z, newDist);
                    pq.offer(new Node(z, distances.get(z)));//Adding element to PriorityQueue

                }
            }
        }

    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public Vertex getNearestVertex(Position currentPosition) {
        Vertex smallestDist = null;
        double smallestDistTOVect = Double.MAX_VALUE;
        for (Vertex v : vertexList) {
            GVector vect = new GVector(v.getPosition(), currentPosition);
            if (vect.length() < smallestDistTOVect) {
                smallestDistTOVect = vect.length();
                smallestDist = v;
            }
        }
        return smallestDist;
    }

    public Collection<GVector> getObstacleEdges() {
        return obstacleEdges;
    }

    public List<Vertex> getTargetVertexes() {
        return this.targetVertexes;
    }

    public void addTarget(Vertex target) {
        this.targetVertexes.add(target);
        this.vertexList.add(target);
    }
}
