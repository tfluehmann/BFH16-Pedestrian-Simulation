package model.areas;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import model.Anchor;
import model.GVector;
import model.Position;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class Area extends Polygon {
    protected Position position;
    protected Position currentCenter;
    protected Set<GVector> edges = new HashSet<>();
    protected Collection<Anchor> anchors;

    private double originX;
    private double originY;

    public Area(double... points) {
        super(points);
        createControlAnchors();
        currentCenter = calculateCentroid();
        this.initDragAndDrop();
    }

    private void initDragAndDrop() {
        this.setOnMousePressed((e) -> {
            this.setCursor(Cursor.CLOSED_HAND);
            this.originX = e.getSceneX();
            this.originY = e.getSceneY();
        });
        this.setOnMouseReleased((e) -> {
            this.calculateEdges();
            if (this instanceof Obstacle) {
                Obstacle obst = (Obstacle) this;
                obst.getEdgePoints().clear();
                obst.getCorners().clear();
                obst.calculateCornersAndVertices();
            }
            this.setCursor(Cursor.HAND);
        });
        this.setOnMouseDragged((event) -> {
            double offsetX = event.getSceneX() - this.originX;
            double offsetY = event.getSceneY() - this.originY;
            this.originX = event.getSceneX();
            this.originY = event.getSceneY();
            this.position.setX(this.getPosition().getXValue() + offsetX);
            this.position.setY(this.getPosition().getYValue() + offsetY);
            this.edges.clear();
            this.calculateEdges();
            translatePoints(offsetX, offsetY);
            this.currentCenter = this.calculateCentroid();
        });

        this.setOnScroll((e) -> {
            double factor = 1.01;
            if (e.getDeltaX() < 0 || e.getDeltaY() < 0) factor = (2.0 - factor) * -1;
            if (e.isShiftDown()) {
                this.rotatePoints(this.getRotate() + factor);
            } else {
                this.scalePoints(this.getScaleX() * (factor), this.getScaleY() * (factor));
            }
            e.consume();
        });
    }

    private void scalePoints(double x, double y) {
        movePoints(new Scale(x, y, currentCenter.getXValue(), currentCenter.getYValue()));
        createControlAnchors();

    }

    private void translatePoints(double translateX, double translateY) {
        movePoints(new Translate(translateX, translateY));
        this.createControlAnchors();
    }

    /**
     * Translate back to origin, Rotate, translate back
     * https://de.wikipedia.org/wiki/Drehmatrix
     */
    private void rotatePoints(double angle) {
        movePoints(new Rotate(angle, currentCenter.getXValue(), currentCenter.getYValue()));
        this.createControlAnchors();
    }

    private void movePoints(Transform t) {
        double[] points, newPoints;
        points = new double[getPoints().size()];
        newPoints = new double[getPoints().size()];
        for (int i = 0; i < getPoints().size(); i++)
            points[i] = getPoints().get(i);
        t.transform2DPoints(points, 0, newPoints, 0, getPoints().size() / 2);
        getPoints().clear();
        for (double d : newPoints)
            getPoints().add(d);
    }

    public Position calculateCentroid() {
        double x = 0.;
        double y = 0.;
        int pointCount = getPoints().size();
        for (int i = 0; i < pointCount; i += 2) {
            x += getPoints().get(i);
            y += getPoints().get(i + 1);
        }
        Position center = new Position(x / pointCount, y / pointCount);
        System.out.println("center position " + center);
        return center;
    }

    public abstract List<Position> getCorners();

    /**
     * from each point to next point-> create gvector --> edge
     */
    public void calculateEdges() {
        this.edges.clear();
        for (int x = 0; x < this.getPoints().size(); x += 2) {
            int y = x + 1;
            int x1 = y + 1;
            if (x1 == this.getPoints().size()) x1 = 0;
            int y1 = x1 + 1;
            Position start = new Position(getPoints().get(x), getPoints().get(y));
            Position end = new Position(getPoints().get(x1), getPoints().get(y1));
            GVector edge = new GVector(start, end);
            edges.add(edge);
        }
    }

    /**
     * returns a new object with the given edges or corners
     *
     * @param edges
     *
     * @return
     */
    public static <T extends Area> T createWithNEdges(int edges, Class<T> type) {
        double[] points = new double[edges * 2];
        int radius = 30;
        double phi = 360 / edges;
        double rad = phi / 180 * Math.PI;
        for (int i = 0, counter = 0; i < edges; i++, counter += 2) {
            points[counter] = Math.cos(i * rad) * radius; //x value
            points[counter + 1] = Math.sin(i * rad) * radius; //y value
        }
        try {
            return type.getDeclaredConstructor(double[].class).newInstance(points);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    // @return a list of anchors which can be dragged around to modify points in the format [x1, y1, x2, y2...]
    private void createControlAnchors() {
        ObservableList<Anchor> anchors = FXCollections.observableArrayList();

        for (int i = 0; i < getPoints().size(); i += 2) {
            final int idx = i;

            DoubleProperty xProperty = new SimpleDoubleProperty(getPoints().get(i));
            DoubleProperty yProperty = new SimpleDoubleProperty(getPoints().get(i + 1));

            xProperty.addListener((ov, oldX, x) -> getPoints().set(idx, (double) x));
            yProperty.addListener((ov, oldY, y) -> getPoints().set(idx + 1, (double) y));

            anchors.add(new Anchor(Color.GOLD, xProperty, yProperty));
        }
        this.anchors = anchors;
    }

    public Set<GVector> getEdges() {
        return this.edges;
    }

    public Position getPosition() {
        return position;
    }

    public Collection<Anchor> getAnchors() {
        return anchors;
    }
}