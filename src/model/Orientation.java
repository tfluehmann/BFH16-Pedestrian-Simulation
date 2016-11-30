package model;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import model.persons.Person;

import java.util.Observable;
import java.util.Observer;

/**
 * @author fluth1
 * @apiNote Middle from a person until the radius in the direction of the target
 */
public class Orientation extends Line implements Observer {
    public static double CENTER_ANGLE = 30;
    private Person person;
    private Rectangle view;

    /**
     * _ _ _
     * |       |
     * o ->    |
     * | _ _ _ |
     * <p>
     * _ = perimeter length
     * room of 2 * 3 Perimeters (probably later in the direction of 60 degrees, simplified)
     */
    public Orientation(Person person) {
        this.person = person;
        updateView();
    }

    public void updateView() {
        GVector direction = new GVector(person.getCurrentPosition(), person.getNextVertexPosition());
        ConfigModel conf = ConfigModel.getInstance();
        view = new Rectangle(person.getCurrentPosition().getXValue(), person.getCurrentPosition().getYValue(), conf.getPersonRadius() * 10, conf.getPersonRadius() * 30);
//        30 deg +, 30 deg minus, check if is in triangle...
        //        view.
//        direction is the vector, rotate the rectangle in this direction
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
