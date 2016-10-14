package model.persons;

import config.ConfigModel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import manager.PathManager;
import manager.PerimeterManager;
import model.GVector;
import model.Perimeter;
import model.Position;
import model.Positionable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class Person extends Circle implements Positionable {
	protected ArrayList<Position> oldPositions = new ArrayList<>();
	protected Position currentPosition;
	protected List<Position> path = new ArrayList<>();
	protected int age;
	protected double speed;
	protected PathManager pathManager = new PathManager();
	protected ConfigModel config = ConfigModel.getInstance();
	// protected Character character;

	protected Perimeter currentPerimeter;


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
	public void doStep() {
		Position p = this.calculateNextPossiblePosition();
		if (p != null) this.setPosition(p);
	}

    /**
     * calculate the next position by reducing the speed if needed
     * @return
     */
    private Position calculateNextPossiblePosition() {
		int tries = 1;
		while (tries < 5) {
			Position nextTarget = this.path.get(0);
			GVector vToNextTarget = new GVector(this.currentPosition.getXValue(),
					this.currentPosition.getYValue(), nextTarget.getXValue(), nextTarget.getYValue());
			double lambda = this.speed / tries++ / vToNextTarget.length();
			Position newPosition = vToNextTarget.getLambdaPosition(lambda);
			if (this.isNewPositionAllowed(newPosition)) {
				return newPosition;
			} else if (tries == 2) {
				/**
				 * Try to walk into the left or right hand position
				 */
				Position leftPos = vToNextTarget.moveParallelLeft(newPosition).getEndPosition();
				Position rightPos = vToNextTarget.moveParallelRight(newPosition).getEndPosition();
				if (this.isNewPositionAllowed(leftPos)) return leftPos;
				if (this.isNewPositionAllowed(rightPos)) return rightPos;
			} else if (tries == 5) {
				//TODO probably implement a better solution
				Position stepBack = vToNextTarget.invert().getLambdaPosition(lambda);
				if (this.isNewPositionAllowed(stepBack)) return stepBack;
			}
		}
		return null;
	}

	private boolean isNewPositionAllowed(Position position) {
		if(position == null) return false;
        if (this.currentPerimeter == null) PerimeterManager.getInstance().movePersonRegistration(this);
        Vector<Perimeter> neighPerimeters = this.currentPerimeter.getNeighbors();
		for (Perimeter perimeter : neighPerimeters)
			for (Person person : perimeter.getRegistredPersons()) {
				if (person.equals(this)) continue;
				boolean collision = this.isColliding(position.getXValue(), position.getYValue(), person);
				if (collision) return false;
			}
		return true;
	}

	/**
	 * Sets a new Position, puts the old position into the list.
	 * moves the person
	 * unregisters in the current perimeter and registers in the new one if needed
	 * @param position
	 */
	private void setPosition(Position position) {
		this.oldPositions.add(new Position(this.currentPosition.getXValue(),
				this.currentPosition.getYValue()));
		this.currentPosition.setX(position.getXValue());
		this.currentPosition.setY(position.getYValue());
		if (this.isInNextPathArea() && !this.isInGoalArea()) this.path.remove(0);
		if (!this.currentPerimeter.isInRange(this.getCurrentPosition()))
			PerimeterManager.getInstance().movePersonRegistration(this);
	}

	public boolean isColliding(double x, double y, Person otherPerson) {
		return Math.abs(x - otherPerson.getCurrentPosition().getXValue()) < this.getRadius() + otherPerson.getRadius() &&
				Math.abs(y - otherPerson.getCurrentPosition().getYValue()) < this.getRadius() + otherPerson.getRadius() &&
				!otherPerson.isInGoalArea();
	}

	public boolean isInNextPathArea() {
		Position nextPosition = this.path.get(0);
		return nextPosition.isInRange(this.currentPosition, this.config.getPersonRadius() * 2);
	}

	public boolean isInGoalArea() {
		Position targetPosition = this.path.get(this.path.size() - 1);
		return targetPosition.isInRange(this.currentPosition, this.config.getPersonRadius() * 2);
	}

	public ArrayList<Position> getOldPositions() {
		return this.oldPositions;
	}

	public void setOldPositions(ArrayList<Position> oldPositions) {
		this.oldPositions = oldPositions;
	}

	public Position getCurrentPosition() {
		return this.currentPosition;
	}

	public void setCurrentPosition(Position currentPosition) {
		this.currentPosition = currentPosition;
	}

	public List<Position> getPath() {
		return this.path;
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

	public boolean intersects(double x, double y) {
		return this.intersects(x, y);
	}

	public Perimeter getCurrentPerimeter() {
		return this.currentPerimeter;
	}

	public void setCurrentPerimeter(Perimeter currentPerimeter) {
		this.currentPerimeter = currentPerimeter;
	}

	public PathManager getPathManager() {
		return this.pathManager;
	}
}
