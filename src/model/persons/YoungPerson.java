package model.persons;

import model.ConfigModel;
import model.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 05.10.2016.
 */
public class YoungPerson extends Person {

    public YoungPerson(double maxHeight, double maxWidth, Position spawnPosition) {
        super(maxHeight, maxWidth,
                ThreadLocalRandom.current().nextDouble(
                        ConfigModel.getInstance().getYoungPersonMinSpeed(),
                        ConfigModel.getInstance().getYoungPersonMaxSpeed()),
                spawnPosition);
    }
}
