package model.areas;

import javafx.scene.shape.*;
import model.Position;
import model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class  Area extends Polygon implements Positionable{
    private Position position;
    protected List<Position> edges =  new ArrayList<>();
    public abstract List<Position> getEdges();


    @Override
    public Position getPosition() {
        return position;

    }

    @Override
    public boolean intersects(double x, double y) {
        return this.contains(x, y);
    }
}
