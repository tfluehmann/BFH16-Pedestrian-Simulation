package model;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Position implements Cloneable {
    private double x;
    private double y;

    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public String toString(){
        return "X coordinate: "+ getX() + " Y coordinate: " + getY();
    }

    public boolean isInRange(Position position, int range) {
        return (this.getX() - position.getX() < range && this.getY() - position.getY()  < range);
    }
}
