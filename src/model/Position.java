package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Position implements Cloneable {

	private final DoubleProperty x;
	private final DoubleProperty y;

    public Position(double x, double y){
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
    }
    public double getXValue() {
	    return this.x.get();
    }

    public DoubleProperty getXProperty() {
	    return this.x;
    }

    public double getYValue() {
	    return this.y.get();
    }

    public DoubleProperty getYProperty() {
	    return this.y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public String toString(){
	    return "X coordinate: " + this.getXValue() + " Y coordinate: " + this.getYValue();
    }


	public boolean isInRange(Position position, double range) {
        return Math.abs(getXValue() - position.getXValue()) < range && Math.abs(getYValue() - position.getYValue()) < range;
    }

    public Position add(Position a) {
	    return new Position(getXValue() + a.getXValue(), getYValue() + a.getYValue());
    }

    public Position multiply(double edgeExtender) {
	    return new Position(getXValue() * edgeExtender, getYValue() * edgeExtender);
    }

    public boolean equals(Object pos) {
        double epsilon = 0.0001;
        Position position = (Position) pos;
        if (position == null) return false;
	    double xComparision = Math.abs(getXValue() - position.getXValue());
	    double yComparision = Math.abs(getYValue() - position.getYValue());
        return xComparision < epsilon && yComparision < epsilon;
    }

    public boolean isEmpty() {
        return !(Double.isFinite(this.getXValue()) || Double.isFinite(this.getYValue()));
    }
}
