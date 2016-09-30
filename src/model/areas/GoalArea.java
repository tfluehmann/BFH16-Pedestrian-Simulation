package model.areas;

import model.Position;

import javafx.scene.paint.Color;

import java.util.List;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public class GoalArea extends Area {
    private double height;
    private double width;
    private Position position;

	public GoalArea(double width, double height, Position position) {
        this.position = position;
        this.height = height;
        this.width = width;
		this.relocate(position.getX(), position.getY());
		this.setFill(Color.GREEN);
		this.getPoints().addAll(new Double[] { 0.0, 0.0, width, 0.0, width, height, 0.0, height });
	}

    @Override
    public List<Position> getEdges() {
        edges.add(new Position(this.width, this.height));
        return edges;
    }
}
