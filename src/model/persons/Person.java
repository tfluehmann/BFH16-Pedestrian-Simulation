package model.persons;

import config.ConfigModel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import manager.PerimeterManager;
import model.GVector;
import model.Perimeter;
import model.Position;
import model.Vertex;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class Person extends Circle {
	protected LinkedList<Position> oldPositions = new LinkedList<>();
	protected Position currentPosition;
	protected int age;
	protected double speed;
	protected PerimeterManager pm = PerimeterManager.getInstance();
	protected LinkedList<Vertex> path = new LinkedList();
	protected PerimeterManager perimeterManager = PerimeterManager.getInstance();

	protected ConfigModel config = ConfigModel.getInstance();
	// protected Character character;


	public Person() {
		super(ConfigModel.getInstance().getPersonRadius(), Color.BLUE);
	}


	public Person(double maxHeight, double maxWidth, double speed, Position spawnPosition) {
		super(ConfigModel.getInstance().getPersonRadius(), Color.BLUE);
		this.speed = speed;
		Random r = new Random();
		double randomWidth = (maxWidth - 2 * this.config.getPersonRadius()) * r.nextDouble();
		double randomHeight = (maxHeight - 2 * this.config.getPersonRadius()) * r.nextDouble();
		this.setCurrentPosition(new Position(randomWidth + spawnPosition.getXValue() + this.config.getPersonRadius(), randomHeight + spawnPosition.getYValue() + this.config.getPersonRadius()));
		this.centerXProperty().bind(this.getCurrentPosition().getXProperty());
		this.centerYProperty().bind(this.getCurrentPosition().getYProperty());
	}

	/**
	 * Calculates the vector and the next position depending on the step size
	 * @return next Position
	 */
	public void calculateStep() {

		Position newPos = this.calculateNextPossiblePosition();
		if (newPos != null) this.setPosition(newPos);
	}

    /**
     * calculate the next position by reducing the speed if needed
     * @return
     */
    private Position calculateNextPossiblePosition() {
		int tries = 1;
		Position nextTarget = path.getFirst().getPosition();
		while (tries < 5) {
			GVector vToNextTarget = new GVector(this.currentPosition.getXValue(),
					this.currentPosition.getYValue(), nextTarget.getXValue(), nextTarget.getYValue());
			double lambda = this.speed / tries / vToNextTarget.length();
			Position newPosition = vToNextTarget.getLambdaPosition(lambda);
			if (this.isNewPositionAllowed(newPosition)) {
				return newPosition;
			} else if (tries >= 2) {
				/**
				 * Try to walk into the left or right hand position
				 */
				Position leftPos = vToNextTarget.moveParallelLeft(newPosition).getEndPosition();
				Position rightPos = vToNextTarget.moveParallelRight(newPosition).getEndPosition();
				if (this.isNewPositionAllowed(leftPos)) {
					System.out.println("second try left: " + tries);
					return leftPos;
				}
				if (this.isNewPositionAllowed(rightPos)) {
					System.out.println("second try right: " + tries);

					return rightPos;
				}
			} else if (tries == 5) {
				System.out.println("step back");
				//TODO probably implement a better solution
				Position stepBack = vToNextTarget.invert().getLambdaPosition(lambda);
				if (this.isNewPositionAllowed(stepBack)) return stepBack;
			}
			tries++;
		}
		return null;
	}

	private boolean isNewPositionAllowed(Position position) {
		if (position == null || position.isEmpty()) return false;
		Set<Perimeter> neighPerimeters = perimeterManager.getNeighbors(position);
//		Perimeter currentPerimeter = perimeterManager.getCurrentPerimeter(position);
//		currentPerimeter.getHorizontalArrayPosition();
//		currentPerimeter.getVerticalArrayPosition();
		for (Perimeter perimeter : neighPerimeters) {
			for (Person person : perimeter.getRegisteredPersons()) {
				if (person.equals(this)) continue;
				boolean collision = this.isColliding(position.getXValue(), position.getYValue(), person);
				//System.out.println(position + " and " + person.getCurrentPosition() + " collision: " + collision);
				if (collision) return false;
			}
		}
		return true;
	}

	/**
	 * Sets a new Position, puts the old position into the list.
	 * moves the person
	 * unregisters in the current perimeter and registers in the new one if needed
	 * @param position
	 */
	public void setPosition(Position position) {
		pm.unregisterPerson(this, pm.getCurrentPerimeter(this.currentPosition));
		this.oldPositions.add(new Position(this.currentPosition.getXValue(),
				this.currentPosition.getYValue()));
		this.currentPosition.setX(position.getXValue());
		this.currentPosition.setY(position.getYValue());
		if (this.isInNextPathArea() && !this.isInGoalArea()) this.path.removeFirst();
		pm.registerPerson(this);
	}

	public boolean isColliding(double x, double y, Person otherPerson) {
		double minimalDistance = config.getPersonRadius() * 2;
		boolean collision = (Math.abs(x - otherPerson.getCurrentPosition().getXValue()) < minimalDistance &&
				Math.abs(y - otherPerson.getCurrentPosition().getYValue()) < minimalDistance &&
				!otherPerson.isInGoalArea());
		return collision;

	}

	public boolean isInNextPathArea() {
		Position nextPosition = path.getFirst().getPosition();
		return nextPosition.isInRange(this.currentPosition, this.config.getPersonRadius());
	}

	public boolean isInGoalArea() {
		Position targetPosition = this.path.getLast().getPosition();
		return targetPosition.isInRange(this.currentPosition, this.config.getPersonRadius());
	}

	public LinkedList<Position> getOldPositions() {
		return this.oldPositions;
	}

	public Position getCurrentPosition() {
		return this.currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setPath(LinkedList<Vertex> path) {
		System.out.println("path for person is " + path);
		this.path = path;
	}
}
