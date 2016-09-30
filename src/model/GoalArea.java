package model;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public class GoalArea extends Area {
    public GoalArea(double height, double width, Position position){
        this.relocate(position.getX(), position.getY());
        this.getPoints().addAll(new Double[]{
                0.0, 0.0,
                0.0, height,
                0.0, width,
                height, width});
    }
}
