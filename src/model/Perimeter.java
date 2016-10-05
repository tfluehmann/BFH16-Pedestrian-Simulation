package model;

import javafx.scene.shape.Shape;

import java.util.*;

/**
 * Created by fluth1 on 30/09/16.
 */
public class Perimeter {
    private static final int NUMBER_OF_PERIMETERS = 50;
    private Position position;
    private double heigth;
    private double width;
   // private List<Shape>


    public Perimeter(Position position, double height, double width){
        this.position = position;
        this.heigth = height;
        this.width = width;
    }

    public static Collection<? extends Perimeter> initializeAll(double roomWidth, double roomHeight) {
        List<Perimeter> perimeters = new ArrayList<>();
        double perimeterWidth = roomWidth / NUMBER_OF_PERIMETERS;
        double perimeterHeight =  roomHeight / NUMBER_OF_PERIMETERS;
        for(int i = 0; i < NUMBER_OF_PERIMETERS; i++)
            perimeters.add(
                    new Perimeter(
                            new Position(i * perimeterWidth, i * perimeterHeight),
                            perimeterHeight, perimeterWidth));
        return perimeters;
    }

    public boolean isInRange(Shape shape){
        return shape.getBoundsInLocal().contains(this.position.getX(), this.position.getY());
    }
}
