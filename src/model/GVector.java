package model;

/**
 * Geometrical Vector Representation for our needs
 * Created by tgdflto1 on 07/10/16.
 */
public class GVector {
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
     * checks if the current and another vector are crossed
     * (currentStartX - otherStartX) / (otherEndX - currentEndX) = tx
     * Same same for y gives the
     *
     * @param vector
     *
     * @return boolean checking java's infinite value
     * http://docs.oracle.com/javase/7/docs/api/java/lang/Double.html
     */
    public boolean isCrossedWith(GVector vector) {
        double tx = (this.getStartPointX() - vector.getStartPointX()) / (vector.getEndPointX() - this.getEndPointX());
        double ty = (this.getStartPointY() - vector.getStartPointY()) / (vector.getEndPointY() - this.getEndPointY());
        return Double.isInfinite(tx) || Double.isInfinite(ty);

    }

    /**
     * @return parallel moved new vector object
     * x
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
