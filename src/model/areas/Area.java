package model.areas;

import javafx.scene.shape.Polygon;
import model.Position;
import model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class  Area extends Polygon implements Positionable{
	protected List<Position> edges = new ArrayList<>();
	private Position position;


	public abstract List<Position> getCorners();


	@Override
	public Position getCurrentPosition() {
		return position;
	}


	@Override
	public boolean intersects(double x, double y) {
		return this.contains(x, y);
	}
}