package model.persons;

import model.Position;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 05.10.2016.
 */
public class HandycappedPerson extends Person {

    public static final double MIN_SPEED = 0.46;
    public static final double MAX_SPEED = 0.77;

    public HandycappedPerson(double maxHeigth, double maxWidth, List<Position> path) {
        super(maxHeigth, maxWidth, path, ThreadLocalRandom.current().nextDouble(MIN_SPEED, MAX_SPEED));
    }
}
