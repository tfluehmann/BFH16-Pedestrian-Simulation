package model.persons;

import model.ConfigModel;
import model.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 05.10.2016.
 * Old Person over 50
 */
public class OldPerson extends Person{

    public OldPerson(Position pos) {
        super(pos, ThreadLocalRandom.current().nextDouble(ConfigModel.getInstance().getOldPersonMinSpeed(), ConfigModel.getInstance().getOldPersonMaxSpeed()));
    }
}
