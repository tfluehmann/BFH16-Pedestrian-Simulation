package model.areas;

import javafx.scene.Cursor;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import model.GVector;
import model.Position;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class Area extends DraggablePolygon {
    protected Set<GVector> edges = new HashSet<>();

    private double originX;
    private double originY;

    public Area(double... points) {
        super(points);
        this.initDragAndDrop();
    }

    private void initDragAndDrop() {
        this.setOnMousePressed((e) -> {
            this.setCursor(Cursor.CLOSED_HAND);
            this.originX = e.getSceneX();
            this.originY = e.getSceneY();
            e.consume();
        });
        this.setOnMouseReleased((e) -> {
            this.setCursor(Cursor.HAND);
            e.consume();
        });
        this.setOnMouseDragged((event) -> {
            double offsetX = event.getSceneX() - this.originX;
            double offsetY = event.getSceneY() - this.originY;
            this.originX = event.getSceneX();
            this.originY = event.getSceneY();
            this.edges.clear();
            this.calculateEdges();
            translatePoints(offsetX, offsetY);
            event.consume();
        });

        this.setOnScroll((e) -> {
            double factor = 1.01;
            if (e.getDeltaY() < 0) factor = (2.0 - factor) * -1;
            double pivotX = e.getX();
            double pivotY = e.getY();
            if (e.isAltDown()) this.rotatePoints(this.getRotate() + factor, pivotX, pivotY);
            else this.scalePoints((factor), (factor), pivotX, pivotY);
            e.consume();
        });
    }

    protected void scalePoints(double x, double y, double pivotX, double pivotY) {
        movePoints(new Scale(x, y, pivotX, pivotY));
    }

    protected void translatePoints(double translateX, double translateY) {
        movePoints(new Translate(translateX, translateY));
    }

    /**
     * Translate back to origin, Rotate, translate back
     * https://de.wikipedia.org/wiki/Drehmatrix
     */
    protected void rotatePoints(double angle, double pivotX, double pivotY) {
        movePoints(new Rotate(angle, pivotX, pivotY));
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

        this.calculateEdges();
        if (this instanceof Obstacle) {
            Obstacle obst = (Obstacle) this;
            obst.getEdgePoints().clear();
            obst.getCorners().clear();
        }
        moveControlAnchors();
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
     * @param edges, type of object
     *
     * @return Any kind of area
     */
    public static <T extends Area> T createWithNEdges(int edges, Class<T> type) {
        double[] newAreaPoints = new double[edges * 2];
        int radius = 30;
        double phi = 360 / edges;
        double rad = phi / 180 * Math.PI;
        for (int i = 0, counter = 0; i < edges; i++, counter += 2) {
            newAreaPoints[counter] = Math.cos(i * rad) * radius; //x value
            newAreaPoints[counter + 1] = Math.sin(i * rad) * radius; //y value
        }
        try {
            return type.getDeclaredConstructor(double[].class).newInstance(newAreaPoints);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    public Set<GVector> getEdges() {
        return this.edges;
    }

    public Position getPosition() {
        return new Position(getPoints().get(0), getPoints().get(1));
    }
}