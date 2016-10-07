package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Position implements Cloneable {

    private DoubleProperty x;
    private DoubleProperty y;

    public Position(double x, double y){
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
    }
    public double getXValue() {
        return x.get();
    }

    public DoubleProperty getXProperty() {
        return x;
    }

    public double getYValue() {
        return y.get();
    }

    public DoubleProperty getYProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public String toString(){
        return "X coordinate: "+ getXValue() + " Y coordinate: " + getYValue();
    }

    public boolean isInRange(Position position, int range) {
        return (this.getXValue() - position.getXValue() < range && this.getYValue() - position.getYValue()  < range);
    }

    public Position add(Position a) {
        return new Position(this.getXValue() + a.getXValue(), this.getYValue() + a.getYValue());
    }

    public Position multiply(double edgeExtender) {
        return new Position(this.getXValue() * edgeExtender, this.getYValue() * edgeExtender);
    }
}
