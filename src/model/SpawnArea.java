package model;

import javafx.scene.paint.Color;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public class SpawnArea extends Area {
	public SpawnArea(double width, double height, Position position) {
		this.setFill(Color.RED);
		this.relocate(position.getX(), position.getY());
		this.getPoints().addAll(new Double[] { 0.0, 0.0, width, 0.0, width, height, 0.0, height });
	}
}
