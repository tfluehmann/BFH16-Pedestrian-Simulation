package model.persons;

import javafx.scene.Cursor;
import javafx.scene.shape.Circle;
import manager.PerimeterManager;
import manager.areamanagers.GoalAreaManager;
import manager.areamanagers.ObstacleManager;
import model.*;
import model.areas.GoalArea;
import model.areas.Obstacle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by fluth1 on 30/09/16.
 * A representation of a person in the simulation
 */
public abstract class Person extends Circle {
    private Orientation orientation;
    private LinkedList<Position> oldPositions = new LinkedList<>();
    private Position currentPosition;
    //    protected int age;
    private double speed;
    private PerimeterManager pm = PerimeterManager.getInstance();
    private PerimeterManager perimeterManager = PerimeterManager.getInstance();
    private ObstacleManager obstacleManager = ObstacleManager.getInstance();
    private GoalAreaManager goalAreaManager = GoalAreaManager.getInstance();
    protected ConfigModel config = ConfigModel.getInstance();
    private boolean inGoal;
    private GoalArea goalArea;


    //	Current Target Vertex
    private Vertex nextVertex;

    //	End Goal
    private TargetVertex target;

    private boolean draggable;

    private double originX;
    private double originY;

    private double targetX;
    private double targetY;

    public Person(Position pos, double speed) {
        super(ConfigModel.getInstance().getPersonRadius());
        getStyleClass().add("person");
        this.speed = speed * config.getPixelPerMeter();
        this.setCurrentPosition(pos);
        this.centerXProperty().bind(this.getCurrentPosition().getXProperty());
        this.centerYProperty().bind(this.getCurrentPosition().getYProperty());
        this.draggable = true;
        this.setCursor(Cursor.HAND);
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        this.setOnMousePressed((e) -> {
            if (this.draggable) {
                this.setCursor(Cursor.CLOSED_HAND);
                this.originX = e.getSceneX();
                this.originY = e.getSceneY();
                this.targetX = this.getCurrentPosition().getXValue();
                this.targetY = this.getCurrentPosition().getYValue();
            }
        });
        this.setOnMouseReleased((e) -> this.setCursor(Cursor.HAND));
        this.setOnMouseDragged((e) -> {
            if (this.draggable) {
                double offsetX = e.getSceneX() - this.originX;
                double offsetY = e.getSceneY() - this.originY;
                double newTranslateX = this.targetX + offsetX;
                double newTranslateY = this.targetY + offsetY;
                this.setPosition(new Position(newTranslateX, newTranslateY));
            }
        });
    }

    /**
     * Calculates the vector and the next position depending on the step size
     */
    public void calculateStep() {
        Position newPos = this.calculateNextPossiblePosition();
        if (newPos != null) this.setPosition(newPos);
        if (this.nextVertex == null || this.isInGoalArea()) setInGoal();
    }

    /**
     * calculate the next position by reducing the speed if needed
     *
     * @return new position
     */
    private Position calculateNextPossiblePosition() {
        Position nextTarget = nextVertex.getPosition();
        boolean isJam = this.orientation.isJam();
        if (isJam && !(nextVertex instanceof TargetVertex)) {
            Vertex nonJamVertex = this.orientation.getDifferentTargetVertex();
            if (nonJamVertex != null) {
                //System.out.println("reset target");
                this.nextVertex = nonJamVertex;
                nextTarget = nonJamVertex.getPosition();
            }
        }
        GVector vToNextTarget = new GVector(this.currentPosition.getXValue(),
                this.currentPosition.getYValue(), nextTarget.getXValue(), nextTarget.getYValue());
        int tries = 1;
        while (tries <= 2) {
            double lambda = (this.speed / tries++) / vToNextTarget.length();
            Position newPosition = vToNextTarget.getLambdaPosition(lambda);
            if (this.isNewPositionAllowed(newPosition))
                return newPosition;

            /*
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
        }
        return null;
    }

    public boolean isNewPositionAllowed(Position position) {
        if (position == null || position.isEmpty()) return false;
        GVector reachable = new GVector(this.currentPosition, position);
        GVector targetInView = new GVector(position, this.getNextVertexPosition());
        if (ObstacleManager.getInstance().isCrossingAnyObstacle(reachable) ||
                ObstacleManager.getInstance().isCrossingAnyObstacle(targetInView)) return false;
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
     * @return list of positions on the radius
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
     * @param position a new position for the person
     */
    public void setPosition(Position position) {
        pm.unregisterPerson(this, pm.getCurrentPerimeter(this.currentPosition));
        this.oldPositions.add(new Position(this.currentPosition.getXValue(),
                this.currentPosition.getYValue()));
        this.currentPosition.setX(position.getXValue());
        this.currentPosition.setY(position.getYValue());
        this.orientation.updateView();
        if (this.isInNextPathArea() && !this.isInGoal())
            this.nextVertex = nextVertex.getNextHopForTarget(this.target);
        pm.registerPerson(this);
    }

    private boolean isColliding(double x, double y, Person otherPerson) {
        double minimalDistance = getDiameter();
        return (Math.abs(x - otherPerson.getCurrentPosition().getXValue()) < minimalDistance &&
                Math.abs(y - otherPerson.getCurrentPosition().getYValue()) < minimalDistance &&
                !otherPerson.isInGoal());
    }

    private boolean isInNextPathArea() {
        Position nextPosition = nextVertex.getPosition();
        return nextPosition.isInRange(this.currentPosition, getDiameter());
    }

    private boolean isInGoalArea() {
        if (isInGoal() || nextVertex == null) return true;
        GoalArea currentGoal = getGoalArea();
        boolean isInGoal;
        if (currentGoal == null)
            isInGoal = this.target.getPosition().isInRange(this.currentPosition, getDiameter());
        else
            isInGoal = currentGoal.intersects(this);
        if (isInGoal) this.setInGoal();
        return isInGoal;
    }

    private GoalArea getGoalArea() {
        if (this.goalArea != null) return this.goalArea;
        for (GoalArea goalArea : goalAreaManager.getGoalAreas())
            if (goalArea.intersects(this.target.getPosition().getXValue(), this.target.getPosition().getYValue(), 1, 1)) {
                this.goalArea = goalArea;
                return goalArea;
            }
        return null;
    }

    public LinkedList<Position> getOldPositions() {
        return this.oldPositions;
    }

    public Position getCurrentPosition() {
        return this.currentPosition;
    }

    private void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setNextVertex(Vertex nextVertex) {
        this.nextVertex = nextVertex;
        orientation = new Orientation(this);
    }

    public void setTarget(TargetVertex target) {
        this.target = target;
    }

    public TargetVertex getTarget() {
        return target;
    }

    //	@return Twice the radius
    private double getDiameter() {
        return 2 * this.config.getPersonRadius();
    }


    public Position getNextVertexPosition() {
        return nextVertex.getPosition();
    }

    public void setDraggable(boolean value) {
        this.draggable = value;
    }

    public boolean isInGoal() {
        return inGoal;
    }

    private void setInGoal() {
        this.setVisible(false);
        this.inGoal = true;
    }
}
