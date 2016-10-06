package model.persons;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import model.Position;

/**
 * Created by fluth1 on 30/09/16.
 */
public class MidAgePerson extends Person {

	public static final double MIN_SPEED = 1.41;
	public static final double MAX_SPEED = 1.55;

	public MidAgePerson(double maxHeigth, double maxWidth, List<Position> path) {
		super(maxHeigth, maxWidth,path, ThreadLocalRandom.current().nextDouble(MIN_SPEED, MAX_SPEED));
	}
}
