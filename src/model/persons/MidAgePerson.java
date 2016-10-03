package model.persons;

import java.util.List;
import java.util.Random;

import model.Position;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public class MidAgePerson extends Person {
	public MidAgePerson(double maxHeigth, double maxWidth, List<Position> path) {
		Random r = new Random();
		double randomWidth = 0 + (maxWidth - PERSON_RADIUS) * r.nextDouble();
		double randomHeight = 0 + (maxHeigth - PERSON_RADIUS) * r.nextDouble();
		this.setCurrentPosition(new Position(randomWidth, randomHeight));
		this.relocate(randomWidth, randomHeight);
		System.out.println("initial position: "+this.currentPosition);
		this.path = path;
		this.speed = 2.0;
	}
}
