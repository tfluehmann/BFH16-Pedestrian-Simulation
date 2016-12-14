package model;

import javafx.scene.shape.Line;
import manager.PathManager;
import manager.PerimeterManager;
import manager.SpawnManager;
import model.persons.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fluth1
 * @apiNote Middle from a person until the radius in the direction of the target
 */
public class Orientation extends Line {
    private Person person;
    private ConfigModel configModel = ConfigModel.getInstance();
    private GVector direction;


    /**
     * _ _ _
     * |       |
     * o ->    |
     * | _ _ _ |
     * <p>
     * _ = perimeter length
     * room of 2 * 3 Perimeters (probably later in the direction of 60 degrees, simplified)
     */
    public Orientation(Person person) {
        this.person = person;
        updateView();
    }

    public void updateView() {
        this.direction = new GVector(person.getCurrentPosition(), person.getNextVertexPosition());
    }

    /**
     * if the next vertex is in the view area (4x PerimeterSize),
     * calculate persons and determine jam based on jam level in properties
     *
     * @return if more people are in the range than allowed
     */
    public boolean isJam() {
        Position target = person.getNextVertexPosition();
        updateView();
        if (direction.length() > configModel.getPerimeterSize() *
                configModel.getPersonViewLengthFactor()) return false;
        List<Perimeter> perimeters = new ArrayList<>();
        perimeters.add(PerimeterManager.getInstance().getCurrentPerimeter(target));
        perimeters.addAll(PerimeterManager.getInstance().getNeighbors(target));
        int peopleCount = 0;
        for (Perimeter perimeter : perimeters)
            peopleCount += perimeter.getRegisteredPersons().size();
        return ConfigModel.getInstance().getJamLevel() <= peopleCount;
    }

    public Vertex getDifferentTargetVertex() {
        updateView();
        Vertex possibleVertex = null;
        PathManager pathManager = SpawnManager.getInstance().getPathManager();
        for (Vertex vertex : pathManager.getVertexList()) {
            if (vertex.getPosition().equals(person.getNextVertexPosition())) continue;
            GVector vector = new GVector(direction.getStartPosition(), vertex.getPosition());
            double angle = Math.toDegrees(direction.dotProductWith(vector));
            System.out.println("Angle to vertex: " + angle);
            if (angle <= configModel.getPersonViewAngle() && angle >= -configModel.getPersonViewAngle() &&
                    person.isNewPositionAllowed(vertex.getPosition())) {
                if (possibleVertex == null) possibleVertex = vertex;
                if (possibleVertex.distanceToTarget() > vertex.distanceToTarget())
                    possibleVertex = vertex;
            }
        }
        if (possibleVertex == null) System.out.println("possible vertex null");
        return possibleVertex;
    }
}
