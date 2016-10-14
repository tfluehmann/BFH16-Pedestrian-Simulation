package model;

import manager.PerimeterManager;
import model.persons.Person;

import java.util.List;
import java.util.Vector;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Perimeter {

    private final Position position;
    private final double height;
    private final double width;
    private final int verticalArrayPosition;
    private final int horizontalArrayPosition;
    private final Vector<Person> registredPersons;


    public Perimeter(Position position, double height, double width, int verticalArrayPosition, int horizontalArrayPosition){
        this.registredPersons = new Vector<>();
        this.position = position;
        this.height = height;
        this.width = width;
        this.verticalArrayPosition = verticalArrayPosition;
        this.horizontalArrayPosition = horizontalArrayPosition;
//        this.heightProperty().set(this.heigth);
//        this.widthProperty().set(this.width);
//        this.relocate(this.position.getXValue(), this.position.getYValue());
//        this.setStyle("-fx-stroke: aqua;");
    }



    public boolean isInRange(Position position){
        return (this.position.getXValue() <= position.getXValue() || position.getXValue() <= this.position.getXValue() + this.width) &&
                (this.position.getYValue() <= position.getYValue() || position.getYValue() <= this.position.getYValue() + this.height);

    }

    public void register(Person person) {
        this.registredPersons.add(person);
        person.setCurrentPerimeter(this);
    }

    public Vector<Perimeter> getNeighbors() {
        PerimeterManager pm  = PerimeterManager.getInstance();
        return pm.getNeighbors(this);
    }

    public int getVerticalArrayPosition() {
        return this.verticalArrayPosition;
    }

    public int getHorizontalArrayPosition() {
        return this.horizontalArrayPosition;
    }

    public List<Person> getRegistredPersons() {
        return this.registredPersons;
    }
}
