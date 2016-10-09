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
		this.getPoints().addAll(new Double[]{this.position.getXValue(), this.position.getYValue(),
				this.position.getXValue() + this.width, this.position.getYValue(),
				this.position.getXValue() + this.width, this.position.getYValue() + this.height,
				this.position.getXValue(), this.position.getYValue() + this.height});
		this.setFill(Color.GREEN);
	}


	public List<Position> getCorners() {
		edges.add(new Position(this.position.getXValue() + this.width / 2, this.position.getYValue() + this.height / 2));
		return edges;
	}

}
