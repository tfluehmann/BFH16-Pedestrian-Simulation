package model.persons;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import manager.PathManager;
import manager.PerimeterManager;
import model.GVector;
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
	protected PathManager pathManager = PathManager.getInstance();

    public Person() {
        super(PERSON_RADIUS, Color.BLUE);
    }

    /**
     * Moved spawn-code of fluth1 from MidAgePerson to the parent class.
     * Created by suter1 on 05.10.2016
     */
    public Person(double maxHeight, double maxWidth, List<Position> path, double speed) {
		this(maxHeight, maxWidth, speed);
		this.path.addAll(path);
    }

	public Person(double maxHeight, double maxWidth, double speed) {
		super(PERSON_RADIUS, Color.BLUE);
		this.speed = speed;
		Random r = new Random();
		double randomWidth = 0 + (maxWidth - PERSON_RADIUS) * r.nextDouble();
		double randomHeight = 0 + (maxHeight - PERSON_RADIUS) * r.nextDouble();
		this.setCurrentPosition(new Position(randomWidth, randomHeight));
		this.centerXProperty().bind(this.getCurrentPosition().getXProperty());
		this.centerYProperty().bind(this.getCurrentPosition().getYProperty());
	}

	/**
	 * Calculates the vector and the next position depending on the step size
	 * @return next Position
	 */
	public void doStep() {
		Position p = calculateNextPossiblePosition();
		System.out.println("next p: " + p);
		if(p != null) setPosition(p);
	}

    /**
	 * calculate the next position by reducing the speed if needed
     * @return
     */
	private Position calculateNextPossiblePosition() {
		int tries = 1;
		while(tries < 5) {
			Position nextTarget = this.path.get(0);
			GVector vToNextTarget = new GVector(this.currentPosition.getXValue(),
					this.currentPosition.getYValue(), nextTarget.getXValue(), nextTarget.getYValue());
			double lambda = (speed / tries++) / vToNextTarget.length();
			Position newPosition = vToNextTarget.getLambdaPosition(lambda);
			if(isNewPositionAllowed(newPosition)){
				return newPosition;
			}else if(tries == 2){
				/**
				 * Try to walk into the left or right hand position
				 */
				Position leftPos = vToNextTarget.moveParallelLeft(newPosition).getEndPosition();
				Position rightPos = vToNextTarget.moveParallelRight(newPosition).getEndPosition();
				if(isNewPositionAllowed(leftPos)) return leftPos;
				if(isNewPositionAllowed(rightPos)) return rightPos;
			} else if (tries == 5) {
				//TODO probably implement a better solution
				Position stepBack = vToNextTarget.invert().getLambdaPosition(lambda);
				if (isNewPositionAllowed(stepBack)) return stepBack;
			}
		}
		return null;
	}

	private boolean isNewPositionAllowed(Position position) {
		if(position == null) return false;
		List<Perimeter> neighPerimeters = this.currentPerimeter.getNeighbors();
		for(Perimeter perimeter : neighPerimeters)
			for(Person person : perimeter.getRegistredPersons()){
				if(person.equals(this)) continue;
				boolean collision = isColliding(position.getXValue(), position.getYValue(), person);
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

	public boolean isColliding(double x, double y, Person otherPerson) {
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

    public int getAge() {
        return age;
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
