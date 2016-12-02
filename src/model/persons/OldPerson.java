package model.persons;

import model.ConfigModel;
import model.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 05.10.2016.
 */
public class OldPerson extends Person{

    public OldPerson(double maxHeight, double maxWidth, Position spawnPosition) {
        super(maxHeight, maxWidth,
                ThreadLocalRandom.current().nextDouble(
                        ConfigModel.getInstance().getOldPersonMinSpeed(),
                        ConfigModel.getInstance().getOldPersonMaxSpeed()),
                spawnPosition);
    }
}
