package model.areas;

import manager.areamanagers.GoalAreaManager;
import model.Position;

import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 */
public class GoalArea extends Area {

	public GoalArea(double... points) {
		super(GoalAreaManager.getInstance(), points);
		super.rotatePoints(-135, points[0], points[1]);
		getStyleClass().add("goal-area");
	}

    public boolean inGoalArea(Position p) {
        return this.intersects(p.getXValue(), p.getYValue(), 1, 1);
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