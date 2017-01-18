package model.areas;

import javafx.scene.shape.Circle;
import manager.areamanagers.GoalAreaManager;
import model.Position;

import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 * A polygon with 4 corners rotated -135 degrees to have a certain rectangle
 */
public class GoalArea extends Area {

	public GoalArea(double... points) {
		super(GoalAreaManager.getInstance(), points);
		super.rotatePoints(-135, points[0], points[1]);
		getStyleClass().add("goal-area");
	}

    public boolean inGoalArea(Position p) {
		return super.pointInArea(p);
	}

	public boolean intersects(Circle circle) {
		double circleDistX = Math.abs(circle.getCenterX() - this.getPoints().get(0));
		double circleDistY = Math.abs(circle.getCenterY() - this.getPoints().get(1));

		double areaWidth = Math.abs(this.getPoints().get(2) - this.getPoints().get(0));
		double areaHeight = Math.abs(this.getPoints().get(3) - this.getPoints().get(1));

		if (circleDistX > (areaWidth / 2 + circle.getRadius())) return false;
		if (circleDistY > (areaHeight / 2 + circle.getRadius())) return false;

		if (circleDistX <= (areaWidth / 2)) return true;
		if (circleDistY <= (areaHeight / 2)) return true;

		double cornerDistSqare = Math.sqrt(circleDistX - areaWidth / 2) + Math.sqrt(circleDistY - areaHeight / 2);
		return cornerDistSqare <= Math.sqrt(circle.getRadius());
	}

    public Position getMiddle (){
		List<Double> pos = this.getPoints();
		double x = (Math.max(pos.get(0), pos.get(4)) - Math.min(pos.get(0), pos.get(4))) / 2;
		double y = (Math.max(pos.get(1), pos.get(7)) - Math.min(pos.get(1), pos.get(7))) / 2;
		return new Position(this.getPosition().getXValue()+x, this.getPosition().getYValue()+y);
    }

	@Override
	public List<Position> getCorners() {
		return null;
	}

}