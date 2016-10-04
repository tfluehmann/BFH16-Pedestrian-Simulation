package model.persons;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Position;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public abstract class Person extends Circle {
	protected ArrayList<Position> oldPositions = new ArrayList<>();
	protected Position currentPosition;
	protected List<Position> path;
	protected int age;
	protected double speed;
	public final static int PERSON_RADIUS = 2;
	// protected Character character;

	public Person() {
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

	public List<Position> getPath() {
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

	public void doStep() {
		Platform.runLater(() -> {
			Position nextTarget = this.path.get(0);
			//System.out.println("target: " + nextTarget);
			double x = nextTarget.getX() - this.currentPosition.getX();
			double y = nextTarget.getY() - this.currentPosition.getY();
			double length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
			//System.out.println("length vector: "+ length);
			double lambda = speed / length;
			this.setPosition(this.currentPosition.getX() + x * lambda,
					this.currentPosition.getY() + y * lambda);
        });
	}

	public void setPosition(double x, double y) {
		this.oldPositions.add(new Position(this.currentPosition.getX(),
				this.currentPosition.getY()));
		this.currentPosition.setX(roundWithDecimalFormat(x));
		this.currentPosition.setY(roundWithDecimalFormat(y));
		if(isInNextPathArea() && !isInGoalArea()) path.remove(0);
		this.relocate(x, y);
	}

	public boolean checkCollision(Person otherPerson){
//		if(Math.abs(this.currentPosition.getX() - otherPerson.getCurrentPosition().getX()) <  )
//		this.currentPosition.getX()
		return false;
	}

	public double roundWithDecimalFormat(double val){
		DecimalFormat df = new DecimalFormat("#0.##");
		return Double.parseDouble(df.format(val));
	}

	public boolean isInNextPathArea(){
		Position nextPosition = path.get(0);
		return nextPosition.isInRange(this.currentPosition, PERSON_RADIUS * 2);
	}
	public boolean isInGoalArea() {
		Position targetPosition = path.get(path.size()-1);
		return targetPosition.isInRange(this.currentPosition, PERSON_RADIUS * 2);
	}
}
