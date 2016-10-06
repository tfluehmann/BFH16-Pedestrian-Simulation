package model.persons;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    protected List<Position> path;
    protected int age;
    protected double speed;
    // protected Character character;

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
        this.relocate(randomWidth, randomHeight);
        //System.out.println("initial position: "+this.currentPosition);

        this.path = new ArrayList<>();
        this.path.addAll(path);
    }

    /**
     * Calculates the vector and the next position depending on the step size
     *
     * @return next Position
     */
    public Position nextPosition() {
        Position nextTarget = this.path.get(0);
        double x = nextTarget.getX() - this.currentPosition.getX();
        double y = nextTarget.getY() - this.currentPosition.getY();
        double length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double lambda = speed / length;
//			if(checkCollision())
        return new Position(this.currentPosition.getX() + x * lambda,
                this.currentPosition.getY() + y * lambda);
    }

    public boolean checkCollision(double x, double y, Person otherPerson) {
        return (Math.abs(x - otherPerson.getPosition().getX()) < this.getRadius() + otherPerson.getRadius() &&
                Math.abs(y - otherPerson.getPosition().getY()) < this.getRadius() + otherPerson.getRadius());
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

    public Position getPosition() {
        return currentPosition;
    }

    public void setPosition(Position position) {
        this.oldPositions.add(new Position(this.currentPosition.getX(),
                this.currentPosition.getY()));
        this.currentPosition.setX(roundWithDecimalFormat(position.getX()));
        this.currentPosition.setY(roundWithDecimalFormat(position.getY()));
        if (isInNextPathArea() && !isInGoalArea()) path.remove(0);
        this.relocate(position.getX(), position.getY());
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
}
