package model;

import javafx.scene.shape.Line;
import manager.PathManager;
import manager.PerimeterManager;
import manager.SpawnManager;
import model.persons.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author fluth1
 * @apiNote Middle from a person until the radius in the direction of the target
 */
public class Orientation extends Line implements Observer {
    private Person person;
    private ConfigModel configModel = ConfigModel.getInstance();
    private GVector direction;
    private Position p1;
    private Position p2;
    private Position p3;

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
        GVector leftHand = GVector.newAfterRotation(direction, configModel.getPersonViewAngle());
        GVector rightHand = GVector.newAfterRotation(direction, -configModel.getPersonViewAngle());
        p1 = direction.getStartPosition();
        p2 = leftHand.getEndPosition();
        p3 = rightHand.getEndPosition();
    }


    @Override
    public void update(Observable o, Object arg) {

    }

    /**
     * if the next vertex is in the view area, calculate persons and determine jam based on jam level in properties
     *
     * @return if more people are in the range than allowed
     */
    public boolean isJam() {
        Position target = person.getNextVertexPosition();
        updateView();
        if (Position.inTriangle(target, p1, p2, p3)) {
            List<Perimeter> perimeters = new ArrayList<>();
            perimeters.add(PerimeterManager.getInstance().getCurrentPerimeter(target));
            perimeters.addAll(PerimeterManager.getInstance().getNeighbors(target));
            int peopleCount = 0;
            for (Perimeter perimeter : perimeters)
                peopleCount += perimeter.getRegisteredPersons().size();
            return ConfigModel.getInstance().getJamLevel() <= peopleCount;
        } else
            return false;
    }

    public Vertex getDifferentTargetVertex() {
        List<Vertex> possibleVertices = new ArrayList<>();
        updateView();
        PathManager pathManager = SpawnManager.getInstance().getPathManager();
        for (Vertex vertex : pathManager.getVertexList()) {
            if (Position.inTriangle(vertex.getPosition(), p1, p2, p3) && !vertex.getPosition().equals(person.getNextVertexPosition()))
                possibleVertices.add(vertex);
        }

        Vertex possibleVertex = null;
        for (Vertex vertex : possibleVertices) {
            if (possibleVertex == null) possibleVertex = vertex;
            else {
                if (possibleVertex.distanceToTarget() > vertex.distanceToTarget())
                    possibleVertex = vertex;
            }
        }
        if (possibleVertex == null) System.out.println("possible vertex null");
        return possibleVertex;
    }
}
