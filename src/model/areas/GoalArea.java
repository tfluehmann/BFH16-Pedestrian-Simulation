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

	public GoalArea(double width, double height, Position position) {
		super(position.getXValue(), position.getYValue(),
				position.getXValue() + width, position.getYValue(),
				position.getXValue() + width, position.getYValue() + height,
				position.getXValue(), position.getYValue() + height);
		this.position = position;
		this.height = height;
		this.width = width;
		this.setFill(Color.GREEN);
	}

	public List<Position> getCorners() {
		return null;
	}


	public Position getGoalPoint() {
		return new Position(this.position.getXValue() + this.width / 2, this.position.getYValue() + this.height / 2);
	}
}