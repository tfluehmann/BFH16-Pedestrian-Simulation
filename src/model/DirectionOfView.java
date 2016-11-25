package model;

import javafx.scene.shape.Line;
import model.persons.Person;

/**
 * Created by tgdflto1 on 25/11/16.
 */
public class DirectionOfView extends Line {
    private Person person;

    public DirectionOfView(Person person) {
        this.person = person;
    }
}
