package model.areas;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import manager.areamanagers.SpawnAreaManager;
import model.Anchor;

import java.util.List;

/**
 * Created by fluht1 on 23/11/16.
 * A polygon that has anchors to drag around
 */
public abstract class DraggablePolygon extends Polygon {
    private List<Anchor> anchors;
    private SpawnAreaManager spawnAreaManager = SpawnAreaManager.getInstance();

    DraggablePolygon(double... points) {
        super(points);
        createControlAnchors();
    }

    /**
     * @return a list of anchors which can be dragged around to modify points in the format [x1, y1, x2, y2...]
     */
    private void createControlAnchors() {
        ObservableList<Anchor> anchors = FXCollections.observableArrayList();

        for (int i = 0; i < getPoints().size(); i += 2) {
            final int idx = i;

            DoubleProperty xProperty = new SimpleDoubleProperty(getPoints().get(i));
            DoubleProperty yProperty = new SimpleDoubleProperty(getPoints().get(i + 1));

            xProperty.addListener((ov, oldX, x) -> getPoints().set(idx, (double) x));
            yProperty.addListener((ov, oldY, y) -> getPoints().set(idx + 1, (double) y));

            anchors.add(new Anchor(Color.GOLD, xProperty, yProperty, (Area) this));
        }
        this.anchors = anchors;
    }

    void moveControlAnchors() {
        spawnAreaManager.removeAnchors(this.anchors);
        this.anchors.clear();
        createControlAnchors();
        spawnAreaManager.addAnchors(anchors);
    }

    public List<Anchor> getAnchors() {
        return anchors;
    }

}
