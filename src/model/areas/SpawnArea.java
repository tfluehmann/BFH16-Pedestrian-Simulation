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


	public SpawnArea(double width, double height, Position position) {
        super(position.getXValue(), position.getYValue(),
                position.getXValue() + width, position.getYValue(),
                position.getXValue() + width, position.getYValue() + height,
                position.getXValue(), position.getYValue() + height);
        this.width = width;
		this.height = height;
		this.position = position;
		this.setFill(Color.RED);
	}

	public List<Position> getCorners() {
		return null;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }
}
