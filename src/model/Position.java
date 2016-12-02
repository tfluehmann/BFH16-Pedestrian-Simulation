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

    /**
     * http://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
     *
     * @param p1
     * @param p2
     * @param p3
     *
     * @return
     */
    private static double sign(Position p1, Position p2, Position p3) {
        return (p1.getXValue() - p3.getXValue()) * (p2.getYValue() - p3.getYValue()) - (p2.getXValue() - p3.getXValue()) * (p1.getYValue() - p3.getYValue());
    }

    /**
     * http://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
     *
     * @param pt position to determine
     * @param v1 positions of triangle
     * @param v2
     * @param v3
     *
     * @return
     */
    public static boolean inTriangle(Position pt, Position v1, Position v2, Position v3) {
        boolean b1, b2, b3;

        b1 = sign(pt, v1, v2) < 0.0d;
        b2 = sign(pt, v2, v3) < 0.0d;
        b3 = sign(pt, v3, v1) < 0.0d;

        return ((b1 == b2) && (b2 == b3));
    }

    public Position add(Position a) {
	    return new Position(getXValue() + a.getXValue(), getYValue() + a.getYValue());
    }

    public Position multiply(double edgeExtender) {
	    return new Position(getXValue() * edgeExtender, getYValue() * edgeExtender);
    }

    public boolean equals(Object pos) {
        double epsilon = ConfigModel.getInstance().getEpsilon();
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
