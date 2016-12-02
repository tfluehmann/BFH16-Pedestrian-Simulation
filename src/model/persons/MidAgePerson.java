package model.persons;

import model.ConfigModel;
import model.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by fluth1 on 30/09/16.
 */
public class MidAgePerson extends Person {

    public MidAgePerson(double maxHeight, double maxWidth, Position spawnPosition) {
        super(maxHeight, maxWidth,
                ThreadLocalRandom.current().nextDouble(
                        ConfigModel.getInstance().getMidAgePersonMinSpeed(),
                        ConfigModel.getInstance().getMidAgePersonMaxSpeed()),
                spawnPosition);
    }
}
