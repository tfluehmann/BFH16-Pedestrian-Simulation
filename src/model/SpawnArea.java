package model;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public class SpawnArea extends Area {
    public SpawnArea(double height, double width, Position position){
        this.relocate(position.getX(), position.getY());
        this.getPoints().addAll(new Double[]{
                0.0, 0.0,
                0.0, height,
                0.0, width,
                height, width});
    }
}
