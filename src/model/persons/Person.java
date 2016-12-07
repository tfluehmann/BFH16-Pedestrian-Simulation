package model.persons;

import javafx.scene.Cursor;
import javafx.scene.shape.Circle;
import manager.PerimeterManager;
import manager.areamanagers.ObstacleManager;
import model.*;
import model.areas.Obstacle;

import java.util.*;

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
			x = newPos.getXValue() - getCurrentPosition().getXValue();
			y = newPos.getYValue() - getCurrentPosition().getYValue();
			this.setPosition(newPos);
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
        System.out.println("next dist: " + nextVertex.distanceToTarget());
        if (isJam && nextVertex.distanceToTarget() != 0) {
            Vertex nonJamVertex = this.orientation.getDifferentTargetVertex();
            if (nonJamVertex != null) {
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
        Position stepBack = vToNextTarget.getLambdaPosition(-lambda / 2);
        if (this.isNewPositionAllowed(stepBack)) return stepBack;
        return null;
    }

    public boolean isNewPositionAllowed(Position position) {
        if (position == null || position.isEmpty()) return false;
        GVector reachable = new GVector(this.currentPosition, position);
        if (ObstacleManager.getInstance().isCrossingAnyObstacle(reachable)) return false;
        Set<Perimeter> neighPerimeters = perimeterManager.getNeighbors(position);
        for (Perimeter perimeter : neighPerimeters) {
            for (Person person : perimeter.getRegisteredPersons()) {
                if (person.equals(this)) continue;
                boolean obstacleCollision = this.isCollidingWithObstacle(position);
                boolean personCollision = this.isColliding(position.getXValue(), position.getYValue(), person);
                if (personCollision || obstacleCollision) return false;
            }
        }
        return true;
    }

    private boolean isCollidingWithObstacle(Position p) {
        List<Position> positions = getPointsOnRadius(p);
        for (Obstacle o : obstacleManager.getObstacles())
            for (Position pos : positions)
                if (o.contains(pos.getXValue(), pos.getYValue())) return true;
        return false;
    }

    /**
     * x = cx + r * cos(a)
     * y = cy + r * sin(a)
     *
     * @return
     */
    private List<Position> getPointsOnRadius(Position pos) {
        double numberOfPoints = 7;
        double angle = 360 / numberOfPoints;
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < numberOfPoints; i++) {
            double x = pos.getXValue() + config.getPersonRadius() * Math.cos(i * angle);
            double y = pos.getYValue() + config.getPersonRadius() * Math.sin(i * angle);
            positions.add(new Position(x, y));
        }
        return positions;
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

    /**
     * @return Twice the radius
     */
    public double getDiameter() {
        return 2 * this.config.getPersonRadius();
    }


    public Position getNextVertexPosition() {
        return nextVertex.getPosition();
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
