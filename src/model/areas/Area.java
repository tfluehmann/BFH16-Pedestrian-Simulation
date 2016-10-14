package model.areas;

import javafx.scene.shape.Polygon;
import model.GVector;
import model.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class Area extends Polygon {
	protected Position position;
	protected Set<GVector> edges = new HashSet<>();


	public Area(double... points) {
		super(points);
//		System.out.println("points: " + getPoints().size());
		this.calculateEdges();
//		System.out.println("edges calculated (" + edges.size() + ")");
	}


	public abstract List<Position> getCorners();

	/**
	 * from each point to next point-> create gvector --> edge
	 */
	public void calculateEdges() {
		System.out.println(this.getPoints());
		for (int x = 0; x < this.getPoints().size(); x += 2) {
			int y = x + 1;
			int x1 = y + 1;
			if (x1 == this.getPoints().size()) x1 = 0;
			int y1 = x1 + 1;
			Position start = new Position(getPoints().get(x), getPoints().get(y));
			Position end = new Position(getPoints().get(x1), getPoints().get(y1));
			GVector edge = new GVector(start, end);
			edges.add(edge);
		}
	}

	public Set<GVector> getEdges() {
		return this.edges;
	}
}