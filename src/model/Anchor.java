package model;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import manager.areamanagers.SpawnAreaManager;
import model.areas.Area;


/**
 * Class copied from here: https://gist.github.com/jewelsea/5375786
 */
public class Anchor extends Circle {
    private final DoubleProperty x, y;
    private Area area;
    private ConfigModel configModel = ConfigModel.getInstance();
    private boolean draggable;

    public Anchor(Color color, DoubleProperty x, DoubleProperty y, Area area) {
        super(x.get(), y.get(), ConfigModel.getInstance().getAnchorRadius());
        this.area = area;
        this.draggable = true;
        setFill(color.deriveColor(configModel.getAnchorColorRed(), configModel.getAnchorColorGreen(),
                configModel.getAnchorColorBlue(), configModel.getAnchorColorOpacity()));
        setStroke(color);
        setStrokeWidth(configModel.getAnchorStrokeWidth());
        setStrokeType(StrokeType.OUTSIDE);

        this.x = x;
        this.y = y;

        x.bind(centerXProperty());
        y.bind(centerYProperty());
        enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
        SpawnAreaManager am = SpawnAreaManager.getInstance();
        final Delta dragDelta = new Delta();
        setOnMousePressed((event) -> {
        	if(this.draggable){
		        dragDelta.x = getCenterX() - event.getX();
		        dragDelta.y = getCenterY() - event.getY();
		        getScene().setCursor(Cursor.MOVE);
	        }
        });

        setOnMouseReleased((event) -> {
        	if(this.draggable){
		        getScene().setCursor(Cursor.HAND);
		        area.calculateEdges();
	        }
        });
        setOnMouseDragged((event) -> {
        	if(this.draggable){
		        double newX = event.getX() + dragDelta.x;
                if (newX > 0 && newX < am.getRoom().getWidth())
                    setCenterX(newX);
		        double newY = event.getY() + dragDelta.y;
                if (newY > 0 && newY < am.getRoom().getHeight())
                    setCenterY(newY);
	        }
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

    public void setDraggable(boolean value){
    	this.draggable = value;
    }
}
