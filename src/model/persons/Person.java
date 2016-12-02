package model.persons;

import javafx.scene.Cursor;
import javafx.scene.shape.Circle;
import manager.PerimeterManager;
import manager.areamanagers.ObstacleManager;
import model.*;
import model.areas.Obstacle;

import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/**
 * Created by fluth1 on 30/09/16.
 */
public abstract class Person extends Circle {
    protected Orientation orientation;
    protected LinkedList<Position> oldPositions = new LinkedList<>();
    protected Position currentPosition;
    protected int age;
    protected double speed;
    protected PerimeterManager pm = PerimeterManager.getInstance();
    protected PerimeterManager perimeterManager = PerimeterManager.getInstance();
    protected ObstacleManager obstacleManager = ObstacleManager.getInstance();

    protected ConfigModel config = ConfigModel.getInstance();

    /**
     * Current Target Vertex
     */
    private Vertex nextVertex;
    /**
     * End Goal
     */
    private Vertex target;

    private double originX;
    private double originY;

    private double targetX;
    private double targetY;

    private int time;
    private double travelledDistance;
    // protected Character character;

    public Person(double maxHeight, double maxWidth, double speed, Position spawnPosition) {
        super(ConfigModel.getInstance().getPersonRadius());
        getStyleClass().add("person");
        this.speed = speed * config.getPixelPerMeter();
        Random r = new Random();
        double randomWidth = (maxWidth - getDiameter()) * r.nextDouble();
        double randomHeight = (maxHeight - getDiameter()) * r.nextDouble();
        this.setCurrentPosition(new Position(randomWidth + spawnPosition.getXValue() + this.config.getPersonRadius(), randomHeight + spawnPosition.getYValue() + this.config.getPersonRadius()));
        this.centerXProperty().bind(this.getCurrentPosition().getXProperty());
        this.centerYProperty().bind(this.getCurrentPosition().getYProperty());
        this.setCursor(Cursor.HAND);
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        this.setOnMousePressed((e) -> {
            this.setCursor(Cursor.CLOSED_HAND);
            this.originX = e.getSceneX();
            this.originY = e.getSceneY();
            this.targetX = this.getCurrentPosition().getXValue();
            this.targetY = this.getCurrentPosition().getYValue();
        });
        this.setOnMouseReleased((e) -> this.setCursor(Cursor.HAND));
        this.setOnMouseDragged((e) -> {
            double offsetX = e.getSceneX() - this.originX;
            double offsetY = e.getSceneY() - this.originY;
            double newTranslateX = this.targetX + offsetX;
            double newTranslateY = this.targetY + offsetY;
            this.setPosition(new Position(newTranslateX, newTranslateY));
        });
    }

    /**
     * Calculates the vector and the next position depending on the step size
     *
     * @return next Position
     */
    public void calculateStep() {
        Position newPos = this.calculateNextPossiblePosition();
        if (newPos != null) {
            double x, y;
            x = Math.abs(Math.abs(newPos.getXValue()) - Math.abs(getCurrentPosition().getXValue()));
            y = Math.abs(Math.abs(newPos.getYValue()) - Math.abs(getCurrentPosition().getYValue()));
            this.travelledDistance = Math.abs(x + y);
            this.setPosition(newPos);
            this.time++;
        }
    }

    /**
     * calculate the next position by reducing the speed if needed
     *
     * @return
     */
    private Position calculateNextPossiblePosition() {
        int tries = 1;
        Position nextTarget = nextVertex.getPosition();
        boolean isJam = this.orientation.isJam();
        System.out.println("JAM is: " + isJam);
        if (isJam) {
            Vertex nonJamVertex = this.orientation.getDifferentTargetVertex();
            if (nextVertex != null) {
                System.out.println("reset target");
                this.nextVertex = nonJamVertex;
                nextTarget = nonJamVertex.getPosition();
            }

        }
        GVector vToNextTarget = new GVector(this.currentPosition.getXValue(),
                this.currentPosition.getYValue(), nextTarget.getXValue(), nextTarget.getYValue());
        double lambda = (this.speed / tries) / vToNextTarget.length();
        Position newPosition = vToNextTarget.getLambdaPosition(lambda);
        if (this.isNewPositionAllowed(newPosition))
            return newPosition;
        /**
         * Try to walk into the left or right hand position
         */
        Position leftPos = vToNextTarget.moveParallelLeft(newPosition).getEndPosition();
        Position rightPos = vToNextTarget.moveParallelRight(newPosition).getEndPosition();
        if (this.isNewPositionAllowed(leftPos)) {
            return leftPos;
        }
        if (this.isNewPositionAllowed(rightPos)) {
            return rightPos;
        }


//        while (tries <= 5) {
//            GVector vToNextTarget = new GVector(this.currentPosition.getXValue(),
//                    this.currentPosition.getYValue(), nextTarget.getXValue(), nextTarget.getYValue());
//            double lambda = (this.speed / tries) / vToNextTarget.length();
//            Position newPosition = vToNextTarget.getLambdaPosition(lambda);
//            if (this.isNewPositionAllowed(newPosition)) {
//				return newPosition;
//			} else if (tries >= 2) {
//				/**
//				 * Try to walk into the left or right hand position
//				 */
//				Position leftPos = vToNextTarget.moveParallelLeft(newPosition).getEndPosition();
//				Position rightPos = vToNextTarget.moveParallelRight(newPosition).getEndPosition();
//				if (this.isNewPositionAllowed(leftPos)) {
//					return leftPos;
//				}
//				if (this.isNewPositionAllowed(rightPos)) {
//					return rightPos;
//				}
//			} else if (tries == 5) {
//				System.out.println("step back");
//				//TODO probably implement a better solution
//				Position stepBack = vToNextTarget.invert().getLambdaPosition(lambda);
//				if (this.isNewPositionAllowed(stepBack)) return stepBack;
//			}
//			tries++;
//		}
        return null;
    }

    private boolean isNewPositionAllowed(Position position) {
        if (position == null || position.isEmpty()) return false;
        Set<Perimeter> neighPerimeters = perimeterManager.getNeighbors(position);
        for (Perimeter perimeter : neighPerimeters) {
            for (Person person : perimeter.getRegisteredPersons()) {
                if (person.equals(this)) continue;
                boolean personCollision = this.isColliding(position.getXValue(), position.getYValue(), person);
                boolean obstacleCollision = this.isCollidingWithObstacle(position);
                if (personCollision || obstacleCollision) return false;
            }
        }
        return true;
    }

    private boolean isCollidingWithObstacle(Position p) {
        for (Obstacle o : obstacleManager.getObstacles())
            if (o.contains(p.getXValue(), p.getYValue())) return true;
        return false;
    }

    /**
     * Sets a new Position, puts the old position into the list.
     * moves the person
     * unregisters in the current perimeter and registers in the new one if needed
     *
     * @param position
     */
    public void setPosition(Position position) {
        pm.unregisterPerson(this, pm.getCurrentPerimeter(this.currentPosition));
        this.oldPositions.add(new Position(this.currentPosition.getXValue(),
                this.currentPosition.getYValue()));
        this.currentPosition.setX(position.getXValue());
        this.currentPosition.setY(position.getYValue());
        if (this.isInNextPathArea() && !this.isInGoalArea())
            this.nextVertex = nextVertex.getNextHopForTarget(this.target);
        pm.registerPerson(this);
    }

    public boolean isColliding(double x, double y, Person otherPerson) {
        double minimalDistance = getDiameter();
        return (Math.abs(x - otherPerson.getCurrentPosition().getXValue()) < minimalDistance &&
                Math.abs(y - otherPerson.getCurrentPosition().getYValue()) < minimalDistance &&
                !otherPerson.isInGoalArea());
    }

    public boolean isInNextPathArea() {
        Position nextPosition = nextVertex.getPosition();
        return nextPosition.isInRange(this.currentPosition, getDiameter());
    }

    public boolean isInGoalArea() {
        Vertex targetVertex = nextVertex.getNextHopForTarget(this.target);
        if (targetVertex != null) return false;
        return nextVertex.getPosition().isInRange(this.currentPosition, getDiameter());
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

    public void setNextVertex(Vertex nextVertex) {
        this.nextVertex = nextVertex;
        orientation = new Orientation(this);
    }

    public void setTarget(Vertex target) {
        this.target = target;
    }

    public double getDiameter() {
        return 2 * this.config.getPersonRadius();
    }

    public int getTime() {
        return this.time;
    }

    public double getTravelledDistance() {
        return this.travelledDistance;
    }

    public Position getNextVertexPosition() {
        return nextVertex.getPosition();
    }
}
