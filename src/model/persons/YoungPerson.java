package model.persons;

import model.ConfigModel;
import model.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by suter1 on 05.10.2016.
 * Young Person from 0 to 30
 */
public class YoungPerson extends Person {

	public YoungPerson(Position pos) {
		super(pos, ThreadLocalRandom.current().nextDouble(ConfigModel.getInstance().getYoungPersonMinSpeed(), ConfigModel.getInstance().getYoungPersonMaxSpeed()));
	}
}
