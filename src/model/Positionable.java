package model;

/**
 * Created by fluth1 on 04/10/16.
 */
public interface Positionable {
    Position getPosition();
    boolean intersects(double x, double y);
}
