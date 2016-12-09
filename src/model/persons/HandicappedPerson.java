package model.persons;

import model.ConfigModel;
import model.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 05.10.2016.
 */
public class HandicappedPerson extends Person {

    public HandicappedPerson(Position pos) {
        super(pos, ThreadLocalRandom.current().nextDouble(ConfigModel.getInstance().getHandicappedPersonMinSpeed(), ConfigModel.getInstance().getHandicappedPersonMaxSpeed()));
    }
}
