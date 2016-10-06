package model.persons;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import manager.PerimeterManager;
import model.Perimeter;
import model.Position;
import model.Positionable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class Person extends Circle implements Positionable {
    public final static int PERSON_RADIUS = 2;
    protected ArrayList<Position> oldPositions = new ArrayList<>();
    protected Position currentPosition;
    protected List<Position> path = new ArrayList<>();
    protected int age;
    protected double speed;
    // protected Character character;

	protected Perimeter currentPerimeter;

    public Person() {
        super(PERSON_RADIUS, Color.BLUE);
    }

    /**
     * Moved spawn-code of fluth1 from MidAgePerson to the parent class.
     * Created by suter1 on 05.10.2016
     */
    public Person(double maxHeight, double maxWidth, List<Position> path, double speed) {
        super(PERSON_RADIUS, Color.BLUE);
        this.speed = speed;
        Random r = new Random();
        double randomWidth = 0 + (maxWidth - PERSON_RADIUS) * r.nextDouble();
        double randomHeight = 0 + (maxHeight - PERSON_RADIUS) * r.nextDouble();
        this.setCurrentPosition(new Position(randomWidth, randomHeight));
        this.centerXProperty().bind(this.getCurrentPosition().getXProperty());
        this.centerYProperty().bind(this.getCurrentPosition().getYProperty());
        this.path.addAll(path);
    }

	/**
	 * Calculates the vector and the next position depending on the step size
	 * @return next Position
	 */
	public void doStep() {
		Position p = calculateNextPossiblePosition();
		if(p != null) setPosition(p);
	}

    /**
	 * calculate the next position by reducing the speed if needed
	 * TODO next step, move the vector parallel and try this point
     * @return
     */
	private Position calculateNextPossiblePosition() {
		int tries = 1;
		while(tries < 5) {
			Position nextTarget = this.path.get(0);
			double x = nextTarget.getXValue() - this.currentPosition.getXValue();
			double y = nextTarget.getYValue() - this.currentPosition.getYValue();
			double length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
			double lambda = (speed / tries++) / length;
			Position newPosition = new Position(this.currentPosition.getXValue() + x * lambda,
					this.currentPosition.getYValue() + y * lambda);
			if(isNewPositionAllowed(newPosition)){
				return newPosition;
			}else if(tries == 2){
				Position leftPos = calculateSideStep(currentPosition.getXValue(), currentPosition.getYValue(),
						newPosition.getXValue(), newPosition.getYValue(), true);
				Position rightPos = calculateSideStep(currentPosition.getXValue(), currentPosition.getYValue(),
						newPosition.getXValue(), newPosition.getYValue(), false);
				if(isNewPositionAllowed(leftPos)) return leftPos;
				if(isNewPositionAllowed(rightPos)) return rightPos;

			}
		}
		return null;
	}

	/**
	 * vector = (5, 3)
	 * start + norm(3, -5) * length and end + norm(3, -5) * length
	 */
	private Position calculateSideStep(double startX, double startY, double targetX, double targetY, boolean isLeft){
		double vectorX = targetX - startX;
		double vectorY = targetY - startY;
		double parallelX;
		double parallelY;
		if(isLeft){
			parallelX = vectorY;
			parallelY = vectorX*(-1);
		}else{
			parallelX = vectorY*(-1);
			parallelY = vectorX;
		}
		double lengthParallel = Math.sqrt(Math.pow(parallelX, 2) + Math.pow(parallelY, 2));
		double normalizedX = 1/lengthParallel * parallelX;
		double normalizedY = 1/lengthParallel * parallelY;
		double newEndVectorX = targetX + normalizedX;
		double newEndVectorY = targetY + normalizedY;
		return new Position(newEndVectorX, newEndVectorY);
	}



    /**
     * Calculates the vector and the next position depending on the step size
     *
     * @return next Position
     */
    public Position nextPosition() {
        Position nextTarget = this.path.get(0);
        double x = nextTarget.getXValue() - this.currentPosition.getXValue();
        double y = nextTarget.getYValue() - this.currentPosition.getYValue();
        double length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double lambda = speed / length;
//			if(checkCollision())
        return new Position(this.currentPosition.getXValue() + x * lambda,
                this.currentPosition.getYValue() + y * lambda);
    }

	private boolean isNewPositionAllowed(Position position) {
		if(position == null) return false;
		List<Perimeter> neighPerimeters = this.currentPerimeter.getNeighbors();
		for(Perimeter perimeter : neighPerimeters)
			for(Person person : perimeter.getRegistredPersons()){
				if(person.equals(this)) continue;
				boolean collision = isCollidating(position.getXValue(), position.getYValue(), person);
				if(collision) return false;
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
		this.currentPosition.setX(roundWithDecimalFormat(position.getXValue()));
		this.currentPosition.setY(roundWithDecimalFormat(position.getYValue()));
		if(isInNextPathArea() && !isInGoalArea()) path.remove(0);
		if(!this.currentPerimeter.isInRange(this.getCurrentPosition())) PerimeterManager.getInstance().movePersonRegistration(this);
	}

	public boolean isCollidating(double x, double y, Person otherPerson){
		return (Math.abs(x - otherPerson.getCurrentPosition().getXValue()) < this.getRadius() + otherPerson.getRadius() &&
				Math.abs(y - otherPerson.getCurrentPosition().getYValue()) < this.getRadius() + otherPerson.getRadius() &&
				!otherPerson.isInGoalArea());
	}

    public double roundWithDecimalFormat(double val) {
        DecimalFormat df = new DecimalFormat("#0.##");
        return Double.parseDouble(df.format(val));
    }

    public boolean isInNextPathArea() {
        Position nextPosition = path.get(0);
        return nextPosition.isInRange(this.currentPosition, PERSON_RADIUS * 2);
    }

    public boolean isInGoalArea() {
        Position targetPosition = path.get(path.size() - 1);
        return targetPosition.isInRange(this.currentPosition, PERSON_RADIUS * 2);
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

	public boolean intersects(double x, double y) {
		return this.intersects(x, y);
	}

	public void setCurrentPerimeter(Perimeter currentPerimeter) {
		this.currentPerimeter = currentPerimeter;
	}

	public Perimeter getCurrentPerimeter() {
		return this.currentPerimeter;
	}
}
