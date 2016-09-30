package model;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public abstract class Person extends Circle {
    protected ArrayList<Position> oldPositions = new ArrayList<>();
	protected Position currentPosition;
	protected ArrayList<Position> path;
	protected int age;
	protected double speed;
    public final static int PERSON_RADIUS = 2;
	// protected Character character;

    public Person(){
        super(PERSON_RADIUS, Color.BLUE);
    }

	public ArrayList<Position> getOldPositions() {
		return oldPositions;
	}

	public void setOldPositions(ArrayList<Position> oldPositions) {
		this.oldPositions = oldPositions;
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public ArrayList<Position> getPath() {
		return path;
	}

	public void setPath(ArrayList<Position> path) {
		this.path = path;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
