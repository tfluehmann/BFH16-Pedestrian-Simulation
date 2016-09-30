package model;



import javafx.scene.layout.Pane;
import model.areas.*;
import model.persons.MidAgePerson;
import model.persons.Person;

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

	public static final int ROOM_HEIGHT = 400;
	public static final int ROOM_WIDTH = 400;

	public static final int GOAL_HEIGHT = 20;
	public static final int GOAL_WIDTH = 50;

	public static final int SPAWN_HEIGHT = 150;
	public static final int SPAWN_WIDTH = 150;
	public Room() {
		super();
		this.setStyle("-fx-background-color: gray;");

		this.setPrefSize(ROOM_WIDTH, ROOM_HEIGHT);
		SpawnArea sa = new SpawnArea(SPAWN_WIDTH, SPAWN_HEIGHT, new Position(0.0, 0.0));
		GoalArea ga = new GoalArea(GOAL_WIDTH, GOAL_HEIGHT, new Position(350.0, 380.0));

		this.getChildren().add(sa);
		this.getChildren().add(ga);
		for (int i = 0; i < 5; i++)
			persons.add(new MidAgePerson(SPAWN_WIDTH, SPAWN_HEIGHT));

		this.getChildren().addAll(persons);


	}

}
