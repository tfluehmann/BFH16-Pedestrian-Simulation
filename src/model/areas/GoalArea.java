package model.areas;

import javafx.scene.paint.Color;
import model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 */
public class GoalArea extends Area {
	private double height;
	private double width;

	public GoalArea(double width, double height, Position position) {
		super(0.0, 0.0, width, 0.0, width, height, 0.0, height);
		this.position = position;
		this.height = height;
		this.width = width;
		this.setFill(Color.GREEN);
	}


	public List<Position> getCorners() {
		List<Position> l = new ArrayList<>();
		l.add(new Position(this.position.getXValue() + this.width / 2, this.position.getYValue() + this.height / 2));
		return l;
	}
}