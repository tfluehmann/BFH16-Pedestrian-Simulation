package model.persons;

import model.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 05.10.2016.
 */
public class HandicappedPerson extends Person {

    private static final double MIN_SPEED = 0.46;
    private static final double MAX_SPEED = 0.77;


    public HandicappedPerson(double maxHeigth, double maxWidth, Position spawnPosition) {
        super(maxHeigth, maxWidth, ThreadLocalRandom.current().nextDouble(MIN_SPEED, MAX_SPEED), spawnPosition);
    }
}
