package model;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public class Room extends Rectangle {

    private ArrayList<Person> persons;
    private ArrayList<Perimeter> perimeters;
    private ArrayList<Area> obstacles;
    private ArrayList<Area> goalAreas;
    private ArrayList<Area> spawnAreas;


}
