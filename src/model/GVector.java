package model;

import javafx.scene.shape.Line;

/**
 * Geometrical Vector Representation for our needs
 * Created by tgdflto1 on 07/10/16.
 */
public class GVector extends Line {
    private final double x;
    private final double y;
    private double startPointX;
    private double startPointY;
    private Position startPosition;
    private Position endPosition;
    private double endPointX;
    private double endPointY;

    private GVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public GVector(double startX, double startY, double endX, double endY) {
        startPointX = startX;
        startPointY = startY;
        endPointX = endX;
        endPointY = endY;
        x = this.endPointX - this.startPointX;
        y = this.endPointY - this.startPointY;
        startPosition = new Position(startX, startY);
        endPosition = new Position(endX, endY);
        startXProperty().bind(startPosition.getXProperty());
        startYProperty().bind(startPosition.getYProperty());
        endXProperty().bind(endPosition.getXProperty());
        endYProperty().bind(endPosition.getYProperty());
    }

    public GVector(Position startPosition, Position endPosition) {
        this(startPosition.getXValue(), startPosition.getYValue(),
                endPosition.getXValue(), endPosition.getYValue());
    }

    /**
     * @return length
     */
    public double length() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /**
     * reduces the vector to a new vector with length 1
     *
     * @return new Vector with length 1
     */
    public GVector norm() {
        return new GVector(getStartPointX(), getStartPointY(), 1 / this.length() * this.x, 1 / this.length() * this.y);
    }

    public GVector invert() {
        return new GVector(getStartPointX(), getStartPointY(), getEndPointX() * -1, getEndPointY() * -1);
    }

    /**
     * @return new vector in the other direction
     */
    public GVector otherEdge() {
        return new GVector(getEndPosition(), getStartPosition());
    }

    /**
     * checks if the current and another vector are crossed using the line formula
     * E = B-A = ( Bx-Ax, By-Ay ) //this
     * F = D-C = ( Dx-Cx, Dy-Cy ) // vector
     * P = ( -Ey, Ex )
     * h = ( (A-C) * P ) / ( F * P )
     * This h number is the key. If h is between 0 and 1, the lines intersect, otherwise they don't.
     * If F*P is zero, of course you cannot make the calculation,
     * but in this case the lines are parallel and therefore only intersect in the obvious cases;
     * @param vector to check with
     *
     * @return boolean
     *
     * checking java's infinite value
     * http://docs.oracle.com/javase/7/docs/api/java/lang/Double.html
     *
     * converted C code from here:
     * http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
     */
    public boolean isCrossedWith(GVector vector) {
        double s02_x, s02_y, s10_x, s10_y, s32_x, s32_y, s_numer, t_numer, denom, t;
        double epsilon = 0.0001;
        s10_x = getEndPointX() - getStartPointX();
        s10_y = getEndPointY() - getStartPointY();
        s32_x = vector.getEndPointX() - vector.getStartPointX();
        s32_y = vector.getEndPointY() - vector.getStartPointY();

        denom = s10_x * s32_y - s32_x * s10_y;
        if (denom == 0) return false; //Collinear

        boolean denomPositive = denom > 0;
        s02_x = getStartPointX() - vector.getStartPointX();
        s02_y = getStartPointY() - vector.getStartPointY();
        s_numer = s10_x * s02_y - s10_y * s02_x;
        if (s_numer < epsilon == denomPositive)
            return false; // No collision

        t_numer = s32_x * s02_y - s32_y * s02_x;
        if (t_numer < epsilon == denomPositive)
            return false; // No collision

        if (s_numer > denom == denomPositive || t_numer > denom == denomPositive)
            return false; // No collision
        // Collision detected
        //t = t_numer / denom;
        //System.out.println("crossing at x " + this.startPointX + (t * s10_x));
        //System.out.println("crossing at y " + this.startPointY + (t * s10_y));
        return true;
    }

    private boolean isOnVector(Position position) {
        double lambdaX = (position.getXValue() - startPointX) / endPointX;
        double lambdaY = (position.getYValue() - startPointY) / endPointY;
        return lambdaX >= 0.0 && lambdaX <= 1.0 && lambdaY >= 0.0 && lambdaY <= 1.0;
    }

    /**
     * @return parallel moved new vector object
     *  x
     * /
     * .---->x
     * <p>
     * vector = (5, 3)
     * start + norm(3, -5) * length and finished + norm(3, -5) * length
     */
    public GVector moveParallelLeft(Position target) {
        GVector parallelNormVector = new GVector(target.getYValue() - getStartPointY(),
                (target.getXValue() - getStartPointX()) * -1);
        return new GVector(
                new Position(this.startPointX, this.startPointY),
                new Position(target.getXValue() + parallelNormVector.norm().getX() * parallelNormVector.length(),
                        target.getYValue() + parallelNormVector.norm().getY() * parallelNormVector.length()));
    }

    /**
     * same same but different as left above
     */
    public GVector moveParallelRight(Position target) {
        GVector parallelNormVector = new GVector((target.getYValue() -
                getStartPointY()) * -1,
                target.getXValue() - getStartPointX());
        return new GVector(
                new Position(this.startPointX, this.startPointY),
                new Position(target.getXValue() + parallelNormVector.norm().getX() * parallelNormVector.length(),
                        target.getYValue() + parallelNormVector.norm().getY() * parallelNormVector.length()));
    }

    public Position getLambdaPosition(double lambda) {
        return new Position(this.startPointX + this.x * lambda, this.startPointY + this.y * lambda);
    }

    /**
     * https://en.wikipedia.org/wiki/Rotation_(mathematics)
     *
     * @param vector initial vector
     * @param angle angle to rotate
     *
     * @return a new rotated vector
     */
    public static GVector newAfterRotation(GVector vector, double angle) {
        double newEndX = vector.getEndPointX() * Math.cos(angle) - vector.getEndPointY() * Math.sin(angle);
        double newEndY = vector.getEndPointX() * Math.sin(angle) + vector.getEndPointY() * Math.cos(angle);
        return new GVector(vector.getStartPosition(), new Position(newEndX, newEndY));
    }

    public double getStartPointX() {
        return this.startPointX;
    }

    public double getStartPointY() {
        return this.startPointY;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getEndPointX() {
        return this.endPointX;
    }

    public double getEndPointY() {
        return this.endPointY;
    }

    public Position getStartPosition() {
        return this.startPosition;
    }

    public Position getEndPosition() {
        return this.endPosition;
    }

    public double dotProductWith(GVector vector) {
        double xProduct = this.getX() * vector.getX();
        double yProduct = this.getY() * vector.getY();
        return Math.acos((xProduct + yProduct) / (this.length() * vector.length()));
    }
}