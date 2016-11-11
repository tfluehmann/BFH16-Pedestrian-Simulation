package model.areas;

import javafx.scene.Cursor;
import javafx.scene.shape.Polygon;
import model.GVector;
import model.Position;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class Area extends Polygon {
	protected Position position;
	protected Set<GVector> edges = new HashSet<>();

	private double originX;
	private double originY;

	private double targetX;
	private double targetY;

	private double currentTranslateX;
	private double currentTranslateY;


	public Area(double... points) {
		super(points);
		this.initDragAndDrop();
	}

	private void initDragAndDrop() {
		this.setOnMousePressed((e) -> {
			this.setCursor(Cursor.CLOSED_HAND);
			this.originX = e.getSceneX();
			this.originY = e.getSceneY();
			this.targetX = this.position.getXValue();
			this.targetY = this.position.getYValue();
		});
		this.setOnMouseReleased((e) -> {
			translatePoints(currentTranslateX, currentTranslateY);

			this.calculateEdges();

			if (this instanceof Obstacle) {
				Obstacle obst = (Obstacle) this;
				obst.getEdgePoints().clear();
				obst.getCorners().clear();
				obst.calculateCornersAndVertices();
			}
			this.setCursor(Cursor.HAND);
		});
		this.setOnMouseDragged((event) -> {
			double offsetX = event.getSceneX() - this.originX;
			double offsetY = event.getSceneY() - this.originY;
			double newTranslateX = this.targetX + offsetX;
			double newTranslateY = this.targetY + offsetY;
			this.position.setX(newTranslateX);
			this.position.setY(newTranslateY);
			this.edges.clear();
			this.calculateEdges();

			this.currentTranslateX = offsetX;
			this.currentTranslateY = offsetY;


		});
	}

	private void translatePoints(double translateX, double translateY) {
		System.out.println("Translate X: " + translateX + " translatey" + translateY);
		List<Double> newPoints = new ArrayList<>();
		for (int i = 0; i < getPoints().size(); i += 2) {
			newPoints.add(getPoints().get(i).doubleValue() + translateX);
			newPoints.add(getPoints().get(i + 1).doubleValue() + translateY);
		}
		getPoints().clear();
		getPoints().addAll(newPoints);
	}

	public abstract List<Position> getCorners();

	/**
	 * from each point to next point-> create gvector --> edge
	 */
	public void calculateEdges() {
		this.edges.clear();
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

	/**
	 * returns a new object with the given edges or corners
	 *
	 * @param edges
	 *
	 * @return
	 */
	public static <T extends Area> T createWithNEdges(int edges, Class<T> type) {
		double[] points = new double[edges * 2];
		int radius = 30;
		double phi = 360 / edges;
		double rad = phi / 180 * Math.PI;
		for (int i = 0, counter = 0; i < edges; i++, counter += 2) {
			points[counter] = Math.cos(i * rad) * radius; //x value
			points[counter + 1] = Math.sin(i * rad) * radius; //y value
		}
		try {
			return type.getDeclaredConstructor(double[].class).newInstance(points);
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}

	public Set<GVector> getEdges() {
		return this.edges;
	}

	public Position getPosition() {
		return position;
	}
}