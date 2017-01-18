package model;

import java.util.Comparator;

/**
 * Created by tgdflto1 on 21/10/16.
 * Node with a vertex and a weight
 */
public class Node implements Comparator<Node> {
    private Vertex vertex;
    private double weight;

    public Node(Vertex vertex, double weigth) {
        this.vertex = vertex;
        this.weight = weigth;
    }

    public Node() {

    }


    @Override
    public int compare(Node node1, Node node2) {
        if (node1.weight < node2.weight)
            return -1;
        if (node1.weight > node2.weight)
            return 1;
        return 0;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public double getWeight() {
        return weight;
    }
}
