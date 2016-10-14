package model.persons;

import model.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 05.10.2016.
 */
public class OldPerson extends Person{

    private static final double MIN_SPEED = 0.68;
    private static final double MAX_SPEED = 1.42;


    public OldPerson(double maxHeight, double maxWidth, Position spawnPosition) {
        super(maxHeight, maxWidth, ThreadLocalRandom.current().nextDouble(MIN_SPEED, MAX_SPEED), spawnPosition);
    }

//    public OldPerson(double maxHeight, double maxWidth) {
//        super(maxHeight, maxWidth, ThreadLocalRandom.current().nextDouble(MIN_SPEED, MAX_SPEED));
//    }
}
