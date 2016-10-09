package model.areas;

import javafx.scene.paint.Color;
import model.Position;

import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 */
public class SpawnArea extends Area {
	private double height;
	private double width;

	private Position position;

	public SpawnArea(double width, double height, Position position) {
		this.width = width;
		this.height = height;
		this.position = position;
		this.getPoints().addAll(new Double[]{this.position.getXValue(), this.position.getYValue(),
				this.position.getXValue() + this.width, this.position.getYValue(),
				this.position.getXValue() + this.width, this.position.getYValue() + this.height,
				this.position.getXValue(), this.position.getYValue() + this.height});
		this.setFill(Color.RED);
	}

	public List<Position> getCorners() {
		return null;
    }
}
