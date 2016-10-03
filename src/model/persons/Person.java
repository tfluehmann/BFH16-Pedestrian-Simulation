package model.persons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Position;

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
		this.path.add(new Position(375.0, 390.0));
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
			System.out.println("target: " + nextTarget);
			double oldX = this.currentPosition.getX();
			double oldY = this.currentPosition.getY();
			double x = nextTarget.getX() - this.currentPosition.getX();
			double y = nextTarget.getY() - this.currentPosition.getY();
			double length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
			System.out.println("length vector: "+ length);
			double lambda = speed / length;
			this.setPosition(roundWithDecimalFormat(this.currentPosition.getX() + x * lambda),
					roundWithDecimalFormat(this.currentPosition.getY() + y * lambda));

			System.out.println("speed: "+ roundWithDecimalFormat(Math.sqrt(
					Math.pow(this.currentPosition.getX()-oldX, 2)+
					Math.pow(this.currentPosition.getY()-oldY, 2)))
			);
			System.out.println("new position: " + this.currentPosition + " lambda: "+ lambda);
			//this.setPosition(this.currentPosition.getX()+1, this.currentPosition.getY()+1);
        });


	}

	public void setPosition(double x, double y) {
		this.currentPosition.setX(x);
		this.currentPosition.setY(y);
		this.relocate(x, y);
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public double roundWithDecimalFormat(double val){
		DecimalFormat df = new DecimalFormat("#0.##");
		return Double.parseDouble(df.format(val));
	}

}
