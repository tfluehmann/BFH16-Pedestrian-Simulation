package model;

import javafx.scene.paint.Color;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public class GoalArea extends Area {
	public GoalArea(double width, double height, Position position) {
		this.relocate(position.getX(), position.getY());
		this.setFill(Color.GREEN);
		this.getPoints().addAll(new Double[] { 0.0, 0.0, width, 0.0, width, height, 0.0, height });
	}
}
