package model.areas;

import model.Position;

import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 */
public class GoalArea extends Area {

	public GoalArea(double width, double height, Position position) {
		super(position.getXValue(), position.getYValue(),
				position.getXValue() + width, position.getYValue(),
				position.getXValue() + width, position.getYValue() + height,
				position.getXValue(), position.getYValue() + height);
		this.position = position;
		getStyleClass().add("goal-area");
	}

	public GoalArea(double... points) {
		super(points);
		this.position = new Position(points[0], points[1]);
		this.setFill(Color.GREEN);
	}

	@Override
	public List<Position> getCorners() {
		return null;
	}

}