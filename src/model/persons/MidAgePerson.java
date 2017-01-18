package model.persons;

import model.ConfigModel;
import model.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by fluth1 on 30/09/16.
 * MidAge Person, usually between 30 and 50
 */
public class MidAgePerson extends Person {

    public MidAgePerson(Position pos) {
        super(pos, ThreadLocalRandom.current().nextDouble(ConfigModel.getInstance().getMidAgePersonMinSpeed(), ConfigModel.getInstance().getMidAgePersonMaxSpeed()));
    }
}
