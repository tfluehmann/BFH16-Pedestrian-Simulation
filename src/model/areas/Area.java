package model.areas;

import controller.DeleteContextMenu;
import javafx.scene.Cursor;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import manager.areamanagers.AreaManager;
import manager.areamanagers.SpawnAreaManager;
import model.GVector;
import model.Position;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fluth1 on 30/09/16.
 * An abstract representation of an Area in the simulation, for example goal or spawn area
 */
public abstract class Area extends DraggablePolygon {
    private Set<GVector> edges = new HashSet<>();
    private boolean draggable;
    private double originX;
    private double originY;

    public Area(AreaManager manager, double... points) {
        super(points);
        this.draggable = true;
        new DeleteContextMenu(manager, this);
        this.initDragAndDrop();
    }

    private void initDragAndDrop() {
        this.setOnMousePressed((e) -> {
            if (this.draggable) {
                this.setCursor(Cursor.CLOSED_HAND);
                this.originX = e.getSceneX();
                this.originY = e.getSceneY();
                e.consume();
            }
        });
        this.setOnMouseReleased((e) -> {
            if (this.draggable) {
                this.setCursor(Cursor.HAND);
                e.consume();
            }
        });
        this.setOnMouseDragged((event) -> {
            if (this.draggable) {
                double offsetX = event.getSceneX() - this.originX;
                double offsetY = event.getSceneY() - this.originY;
                this.originX = event.getSceneX();
                this.originY = event.getSceneY();

                this.edges.clear();
                this.calculateEdges();
                translatePoints(offsetX, offsetY);
                event.consume();
            }
        });

        this.setOnScroll((e) -> {
            if (this.draggable) {
                double factor = 1.01;
                if (e.getDeltaY() < 0) factor = (2.0 - factor);
                double pivotX = e.getX();
                double pivotY = e.getY();
                if (e.isAltDown()) this.rotatePoints(this.getRotate() + factor, pivotX, pivotY);
                else this.scalePoints((factor), (factor), pivotX, pivotY);
                e.consume();
            }
        });
    }

    private boolean pointsInBounds(double[] points) {
        SpawnAreaManager am = SpawnAreaManager.getInstance();
        double roomHeight = am.getRoom().getHeight();
        double roomWidth = am.getRoom().getWidth();
        double originX = am.getRoom().getLayoutX();
        double originY = am.getRoom().getLayoutY();
        for (int x = 0, y = 1; x < points.length - 2; x += 2, y += 2) {
            double xCoordinate = points[x] + originX;
            double yCoordinate = points[y] + originY;
            if (xCoordinate < originX || xCoordinate > originX + roomWidth) return false;
            if (yCoordinate < originY || yCoordinate > originY + roomHeight) return false;
        }
        return true;
    }

    private void scalePoints(double x, double y, double pivotX, double pivotY) {
        movePoints(new Scale(x, y, pivotX, pivotY));
    }

    private void translatePoints(double translateX, double translateY) {
        movePoints(new Translate(translateX, translateY));
    }

    /**
     * Translate back to origin, Rotate, translate back
     * https://de.wikipedia.org/wiki/Drehmatrix
     * @param angle to rotate
     * @param pivotX x coordinate of the rotation point
     * @param pivotY y coordinate of the rotation point
     */
    void rotatePoints(double angle, double pivotX, double pivotY) {
        movePoints(new Rotate(angle, pivotX, pivotY));
    }

    private void movePoints(Transform t) {
        double[] points, newPoints;
        points = new double[getPoints().size()];
        newPoints = new double[getPoints().size()];
        for (int i = 0; i < getPoints().size(); i++)
            points[i] = getPoints().get(i);
        t.transform2DPoints(points, 0, newPoints, 0, getPoints().size() / 2);

        if (pointsInBounds(newPoints)) {

            getPoints().clear();
            for (double d : newPoints)
                getPoints().add(d);

            this.calculateEdges();
            if (this instanceof Obstacle) {
                Obstacle obstacle = (Obstacle) this;
                obstacle.getEdgePoints().clear();
                obstacle.getCorners().clear();
            }
            moveControlAnchors();
        }
    }

    public abstract List<Position> getCorners();

    /**
     * from each point to next point - create g-vector - edge
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
     * https://de.wikipedia.org/wiki/Punkt-in-Polygon-Test_nach_Jordan
     *
     * @param position the position you want to check in the area
     *
     * @return a boolean that says if the position is in the area or not
     */
    boolean pointInArea(Position position) {
        int t = -1;
        for (int x = 0, y = 1; x <= this.getPoints().size() - 2; x += 2, y += 2) {
            Position b = new Position(this.getPoints().get(x), this.getPoints().get(y));
            Position c;
            if (x < this.getPoints().size() - 2)
                c = new Position(this.getPoints().get(x + 2), this.getPoints().get(y + 2));
            else
                c = new Position(this.getPoints().get(0), this.getPoints().get(1));
            t = t * crossProduct(position, b, c);
        }
        return t == 0;
    }

    private int crossProduct(Position a, Position b, Position c) {
        if (a.getYValue() == b.getYValue() && b.getYValue() == c.getYValue()) {
            if ((a.getXValue() <= b.getXValue() && b.getXValue() <= c.getXValue()) ||
                    (c.getXValue() <= a.getXValue() && a.getXValue() <= b.getXValue()))
                return 0;
            else
                return 1;
        }
        if (b.getYValue() > c.getYValue()) {
            Position tmp = c;
            c = b;
            b = tmp;
        }
        if (a.getYValue() == b.getYValue() && a.getXValue() == b.getXValue()) return 0;
        if (a.getYValue() <= b.getYValue() || a.getYValue() > c.getYValue()) return 1;
        double delta = (b.getXValue() - a.getXValue()) * (c.getYValue() - a.getYValue()) -
                (b.getYValue() - a.getYValue()) * (c.getXValue() - a.getXValue());
        if (delta > 0)
            return -1;
        else if (delta < 0)
            return 1;
        else return 0;
    }

    /**
     * returns a new object with the given edges or corners
     *
     * @param edges, type of object
     * @param type type of object it should create
     * @return Any kind of area
     */
    public static <T extends Area> T createWithNEdges(int edges, Class<T> type) {
        double[] newAreaPoints = new double[edges * 2];
        int radius = 30;
        double phi = 360 / edges;
        double rad = phi / 180 * Math.PI;
        for (int i = 0, counter = 0; i < edges; i++, counter += 2) {
            newAreaPoints[counter] = Math.cos(i * rad) * radius + radius; //x value
            newAreaPoints[counter + 1] = Math.sin(i * rad) * radius + radius; //y value
        }
        try {
            return type.getDeclaredConstructor(double[].class).newInstance((Object) newAreaPoints);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    public void setDraggable(Boolean value) {
        this.draggable = value;
    }

    public Set<GVector> getEdges() {
        return this.edges;
    }

    public Position getPosition() {
        return new Position(getPoints().get(0), getPoints().get(1));
    }

    public String toString() {
        return this.getPoints().toString();
    }
}