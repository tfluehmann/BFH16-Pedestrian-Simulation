package model.areas;

import javafx.scene.paint.Color;
import model.Position;

import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 */
public class GoalArea extends Area {
	private double height;
	private double width;

	private Position position;

	public GoalArea(double width, double height, Position position) {
		this.position = position;
		this.height = height;
		this.width = width;
		this.relocate(position.getXValue(), position.getYValue());
		this.setFill(Color.GREEN);
		this.getPoints().addAll(new Double[] { 0.0, 0.0, width, 0.0, width, height, 0.0, height });
	}

	public List<Position> getCorners() {
		edges.add(new Position(this.position.getXValue() + this.width / 2, this.position.getYValue() + this.height / 2));
		return edges;
	}


}
