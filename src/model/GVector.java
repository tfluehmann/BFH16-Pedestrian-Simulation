package model;

import javafx.scene.shape.Line;

/**
 * Geometrical Vector Representation for our needs
 * Created by tgdflto1 on 07/10/16.
 */
public class GVector extends Line {
    public static final double EPSILON = 0.00001;

    private double startPointX;
    private double startPointY;
    private Position startPosition;
    private double x;
    private double y;
    private Position endPosition;
    private double endPointX;
    private double endPointY;

    private GVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public GVector(double startX, double startY, double endX, double endY) {
        this.startPointX = startX;
        this.startPointY = startY;
        this.endPointX = endX;
        this.endPointY = endY;
        this.x = (endPointX - startPointX);
        this.y = (endPointY - startPointY);
        this.startPosition = new Position(startX, startY);
        this.endPosition = new Position(endX, endY);
        this.startXProperty().bind(this.startPosition.getXProperty());
        this.startYProperty().bind(this.startPosition.getYProperty());
        this.endXProperty().bind(this.endPosition.getXProperty());
        this.endYProperty().bind(this.endPosition.getYProperty());
    }

    public GVector(Position startPosition, Position endPosition) {
        this(startPosition.getXValue(), startPosition.getYValue(),
                endPosition.getXValue(), endPosition.getYValue());
    }

    /**
     * @return length
     */
    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
     * reduces the vector to a new vector with length 1
     *
     * @return
     */
    public GVector norm() {
        return new GVector(this.getStartPointX(), this.getStartPointY(), 1 / length() * x, 1 / length() * y);
    }

    public GVector invert() {
        return new GVector(this.getStartPointX(), this.getStartPointY(), this.getEndPointX() * (-1), this.getEndPointY() * (-1));
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
     * @param vector
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
        s10_x = this.getEndPointX() - this.getStartPointX();
        s10_y = this.getEndPointY() - this.getStartPointY();
        s32_x = vector.getEndPointX() - vector.getStartPointX();
        s32_y = vector.getEndPointY() - vector.getStartPointY();

        denom = s10_x * s32_y - s32_x * s10_y;
        if (denom == 0) return false; //Collinear

        boolean denomPositive = denom > 0;
        s02_x = this.getStartPointX() - vector.getStartPointX();
        s02_y = this.getStartPointY() - vector.getStartPointY();
        s_numer = s10_x * s02_y - s10_y * s02_x;
        if ((s_numer < epsilon) == denomPositive)
            return false; // No collision

        t_numer = s32_x * s02_y - s32_y * s02_x;
        if ((t_numer < epsilon) == denomPositive)
            return false; // No collision

        if (((s_numer > denom) == denomPositive) || ((t_numer > denom) == denomPositive))
            return false; // No collision
        // Collision detected
        t = t_numer / denom;
        //System.out.println("crossing at x " + this.startPointX + (t * s10_x));
        //System.out.println("crossing at y " + this.startPointY + (t * s10_y));
        return true;
    }

    private boolean isOnVector(Position position) {
        double lambdaX = (position.getXValue() - this.startPointX) / this.endPointX;
        double lambdaY = (position.getYValue() - this.startPointY) / this.endPointY;
        System.out.println("lx = " + lambdaX + " ly= " + lambdaY);
        return (lambdaX >= 0.0 && lambdaX <= 1.0 && lambdaY >= 0.0 && lambdaY <= 1.0);
    }

    /**
     * @return parallel moved new vector object
     *  x
     * /
     * .---->x
     * <p>
     * vector = (5, 3)
     * start + norm(3, -5) * length and end + norm(3, -5) * length
     */
    public GVector moveParallelLeft(Position target) {
        GVector parallelNormVector = new GVector(target.getYValue() - this.getStartPointY(),
                (target.getXValue() - this.getStartPointX()) * (-1));
        return new GVector(
                new Position(startPointX, startPointY),
                new Position(target.getXValue() + parallelNormVector.norm().getX() * parallelNormVector.length(),
                        target.getYValue() + parallelNormVector.norm().getY() * parallelNormVector.length()));
    }

    /**
     * same same but different as left above
     */
    public GVector moveParallelRight(Position target) {
        GVector parallelNormVector = new GVector((target.getYValue() -
                this.getStartPointY()) * (-1),
                (target.getXValue() - this.getStartPointX()));
        return new GVector(
                new Position(startPointX, startPointY),
                new Position(target.getXValue() + parallelNormVector.norm().getX() * parallelNormVector.length(),
                        target.getYValue() + parallelNormVector.norm().getY() * parallelNormVector.length()));
    }

    public Position getLambdaPosition(double lambda) {
        return new Position(startPointX + x * lambda, startPointY + y * lambda);
    }

    public double getStartPointX() {
        return startPointX;
    }

    public double getStartPointY() {
        return startPointY;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getEndPointX() {
        return endPointX;
    }

    public double getEndPointY() {
        return endPointY;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }
}