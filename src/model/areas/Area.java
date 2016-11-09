package model.areas;

import javafx.scene.Cursor;
import javafx.scene.shape.Polygon;
import model.GVector;
import model.Position;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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


	public Area(double... points) {
		super(points);
		this.calculateEdges();
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
			this.setCursor(Cursor.HAND);
		});
		this.setOnMouseDragged((event) -> {
			double offsetX = event.getSceneX() - this.originX;
			double offsetY = event.getSceneY() - this.originY;
			double newTranslateX = this.targetX + offsetX;
			double newTranslateY = this.targetY + offsetY;
			this.position.setX(newTranslateX);
			this.position.setY(newTranslateY);
			this.relocate(newTranslateX, newTranslateY);
		});
	}


	public abstract List<Position> getCorners();

	/**
	 * from each point to next point-> create gvector --> edge
	 */
	public void calculateEdges() {
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
		int radius = 20;
		double phi = 360 / edges;
		double rad = phi / 180 * Math.PI;
		for (int i = 0, counter = 0; i < edges; i++, counter += 2) {
			double x = Math.cos(i * rad) * radius;
			double y = Math.sin(i * rad) * radius;
			points[counter] = x;
			points[counter + 1] = y;
		}
		try {
			Constructor[] constructors = type.getConstructors();
			Constructor<T> constructor = null;
			for (Constructor c : constructors) {
				System.out.println(c.toGenericString());
				System.out.println(c.getGenericParameterTypes());
				if (c.isVarArgs()) {
					constructor = c;
					break;
				}
			}
//			Constructor<T> constructor = type.getDeclaredConstructor(double[].class);
			return (T) constructor.newInstance(new Object[]{points});
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}

	public Set<GVector> getEdges() {
		return this.edges;
	}
}