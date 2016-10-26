package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.persons.Person;

import java.util.Vector;

/**
 * Created by fluth1 on 30/09/16.
 * Grid on the base shape helps to reduce calculation between persons
 */
public class Perimeter extends Rectangle {

    private final Position position;
    private final double height;
    private final double width;
    private final int verticalArrayPosition;
    private final int horizontalArrayPosition;
    private final Vector<Person> registeredPersons;


    public Perimeter(Position position, double height, double width, int verticalArrayPosition, int horizontalArrayPosition){
        this.registeredPersons = new Vector<>();
        this.position = position;
        this.height = height;
        this.width = width;
        this.verticalArrayPosition = verticalArrayPosition;
        this.horizontalArrayPosition = horizontalArrayPosition;
        this.heightProperty().set(this.height);
        this.widthProperty().set(this.width);
        this.xProperty().bind(this.position.getXProperty());
        this.yProperty().bind(this.position.getYProperty());
        this.setStyle("-fx-stroke: aqua; -fx-background-color: transparent;");
        this.setFill(Color.TRANSPARENT);

    }

    public boolean isInRange(Position position){
        return ((this.position.getXValue() <= position.getXValue()) &&
                (position.getXValue() <= (this.position.getXValue() + this.width)) &&
                (this.position.getYValue() <= position.getYValue()) &&
                (position.getYValue() <= (this.position.getYValue() + this.height)));
    }

    public void register(Person person) {
        if (!this.registeredPersons.contains(person)) this.registeredPersons.add(person);
    }

    public void unregister(Person person) {
        if (this.registeredPersons.contains(person)) this.registeredPersons.remove(person);
    }

    public int getVerticalArrayPosition() {
        return this.verticalArrayPosition;
    }

    public int getHorizontalArrayPosition() {
        return this.horizontalArrayPosition;
    }

    public Vector<Person> getRegisteredPersons() {
        return registeredPersons;
    }
}
