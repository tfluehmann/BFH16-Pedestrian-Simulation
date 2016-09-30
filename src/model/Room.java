package model;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public class Room extends Pane {

    private ArrayList<Person> persons = new ArrayList<>();
    private ArrayList<Perimeter> perimeters;
    private ArrayList<Area> obstacles;
    private ArrayList<Area> goalAreas;
    private ArrayList<Area> spawnAreas;

    public Room() {
        super();
        this.setStyle("-fx-background-color: gray;");

        this.setPrefSize(400,400);
        SpawnArea sa = new SpawnArea(200, 200, new Position(0, 0));
        for(int i = 0; i < 5; i++)
            persons.add(new MidAgePerson(200, 200));
        this.getChildren().addAll(persons);

    }

}
