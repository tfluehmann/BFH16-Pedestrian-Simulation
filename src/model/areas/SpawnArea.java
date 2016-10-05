package model.areas;

import javafx.scene.paint.Color;
import model.Position;

import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 */
public class SpawnArea extends Area {
	public SpawnArea(double width, double height, Position position) {
		this.setFill(Color.RED);
		this.relocate(position.getX(), position.getY());
		this.getPoints().addAll(new Double[] { 0.0, 0.0, width, 0.0, width, height, 0.0, height });
	}

    @Override
    public List<Position> getEdges() {
        return null;
    }
}
