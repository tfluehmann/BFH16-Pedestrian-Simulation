package model;

import manager.PerimeterManager;
import model.persons.Person;

import java.util.List;
import java.util.Vector;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Perimeter {
    public static final int PERIMETER_WIDHT = 2;
    public static final int PERIMETER_HEIGHT = 2;
    private Position position;
    private double heigth;
    private double width;
    private int verticalArrayPosition;
    private int horizontalArrayPosition;
    private Vector<Person> registredPersons = new Vector<>();


    public Perimeter(Position position, double height, double width, int verticalArrayPosition, int horizontalArrayPosition){
        this.position = position;
        this.heigth = height;
        this.width = width;
        this.verticalArrayPosition = verticalArrayPosition;
        this.horizontalArrayPosition = horizontalArrayPosition;
//        this.heightProperty().set(this.heigth);
//        this.widthProperty().set(this.width);
//        this.relocate(this.position.getXValue(), this.position.getYValue());
//        this.setStyle("-fx-stroke: aqua;");
    }



    public boolean isInRange(Position position){
        return (((this.position.getXValue() <= position.getXValue()) || (position.getXValue() <= this.position.getXValue() + width)) &&
                ((this.position.getYValue() <= position.getYValue()) || (position.getYValue() <= this.position.getYValue() + heigth)));

    }

    public void register(Person person) {
        registredPersons.add(person);
        person.setCurrentPerimeter(this);
    }

    public Vector<Perimeter> getNeighbors() {
        PerimeterManager pm  = PerimeterManager.getInstance();
        return pm.getNeighbors(this);
    }

    public int getVerticalArrayPosition() {
        return verticalArrayPosition;
    }

    public int getHorizontalArrayPosition() {
        return horizontalArrayPosition;
    }

    public List<Person> getRegistredPersons() {
        return registredPersons;
    }
}
