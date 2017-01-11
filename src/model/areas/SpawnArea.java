package model.areas;

import manager.areamanagers.SpawnAreaManager;
import model.Position;

import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 */
public class SpawnArea extends Area {

    public SpawnArea(double... points) {
        super(SpawnAreaManager.getInstance(), points);
		super.rotatePoints(-135, points[0], points[1]);
		getStyleClass().add("spawn-area");
	}

	public double getHeight() {
		return Math.abs(getPoints().get(getPoints().size() - 1) - getPoints().get(1));
	}

	public double getWidth() {
		return Math.abs(getPoints().get(2) - getPoints().get(0));
	}

	public List<Position> getCorners() {
		return null;
    }

}
