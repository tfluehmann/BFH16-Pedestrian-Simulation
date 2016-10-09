package model.persons;

import model.Position;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 05.10.2016.
 */
public class OldPerson extends Person{

    public static final double MIN_SPEED = 0.68;
    public static final double MAX_SPEED = 1.42;


    public OldPerson(double maxHeight, double maxWidth, List<Position> path, Position spawnPosition) {
        super(maxHeight, maxWidth, path, ThreadLocalRandom.current().nextDouble(MIN_SPEED, MAX_SPEED), spawnPosition);
    }
}
