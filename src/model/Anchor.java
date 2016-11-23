package model;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;


/**
 * Class copied from here: https://gist.github.com/jewelsea/5375786
 */
public class Anchor extends Circle {
    private final DoubleProperty x, y;

    public Anchor(Color color, DoubleProperty x, DoubleProperty y) {
        super(x.get(), y.get(), 2);
        setFill(color.deriveColor(1, 1, 1, 0.3));
        setStroke(color);
        setStrokeWidth(2);
        setStrokeType(StrokeType.OUTSIDE);

        this.x = x;
        this.y = y;

        x.bind(centerXProperty());
        y.bind(centerYProperty());
        enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
        final Delta dragDelta = new Delta();
        setOnMousePressed((event) -> {
            dragDelta.x = getCenterX() - event.getX();
            dragDelta.y = getCenterY() - event.getY();
            getScene().setCursor(Cursor.MOVE);
        });

        setOnMouseReleased((event) -> {
            getScene().setCursor(Cursor.HAND);
        });
        setOnMouseDragged((event) -> {
            double newX = event.getX() + dragDelta.x;
            if (newX > 0 && newX < getScene().getWidth())
                setCenterX(newX);
            double newY = event.getY() + dragDelta.y;
            if (newY > 0 && newY < getScene().getHeight())
                setCenterY(newY);
        });
        setOnMouseEntered((event) -> {
            if (!event.isPrimaryButtonDown()) getScene().setCursor(Cursor.HAND);
        });
        setOnMouseExited((event) -> {
            if (!event.isPrimaryButtonDown()) getScene().setCursor(Cursor.DEFAULT);

        });
    }

    private class Delta {
        double x, y;
    }
}
