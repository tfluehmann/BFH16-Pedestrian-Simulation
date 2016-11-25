package model.areas;

import model.Position;

import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 */
public class GoalArea extends Area {

	public GoalArea(double... points) {
		super(points);
		super.rotatePoints(-135, points[0], points[1]);
		getStyleClass().add("goal-area");
	}

	@Override
	public List<Position> getCorners() {
		return null;
	}

}