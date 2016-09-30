package model.areas;

import javafx.scene.shape.*;
import model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public abstract class  Area extends Polygon {
    protected List<Position> edges =  new ArrayList<>();
    public abstract List<Position> getEdges();

}
