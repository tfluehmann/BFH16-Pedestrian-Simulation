package model;

import manager.SpawnManager;
import model.persons.*;

import java.util.Vector;

/**
 * Created by suter1 on 25.11.2016.
 */
public class Statistic {

	private static Statistic instance = null;
	private Vector<Person> young;
	private Vector<Person> midage;
	private Vector<Person> old;
	private Vector<Person> handicapped;
	private Vector<Person> personList;


	private Statistic() {
		personList = new Vector<>();
		young = new Vector<>();
		midage = new Vector<>();
		old = new Vector<>();
		handicapped = new Vector<>();
		calculatePersons();
	}


	public static Statistic getInstance() {
		if (instance == null) {
			instance = new Statistic();
		}
		return instance;
	}

	public void calculatePersons() {
		this.personList.clear();
		this.young.clear();
		this.midage.clear();
		this.old.clear();
		this.handicapped.clear();
		Vector<Person> activePersons = SpawnManager.getInstance().getPersons();
		Vector<Person> passivePersons = SpawnManager.getInstance().getPassivePersons();
		this.personList.addAll(activePersons);
		this.personList.addAll(passivePersons);
		for (Person p : personList) {
			if (p instanceof YoungPerson) this.young.add(p);
			else if (p instanceof MidAgePerson) this.midage.add(p);
			else if (p instanceof OldPerson) this.old.add(p);
			else if (p instanceof HandicappedPerson) this.handicapped.add(p);
		}
	}

	private double calculateAvgSpeed(Vector<Person> persons) {
		double speedSum = 0.0;
		for (Person p : persons) {
			speedSum += (p.getTravelledDistance() / p.getTime());
		}
		return speedSum / persons.size();
	}


	public double getYoungSpeedAvg() {
		return calculateAvgSpeed(this.young);
	}

	public double getMidageSpeedAvg(){
		return calculateAvgSpeed(this.midage);
	}

	public double getOldSpeedAvg(){
		return calculateAvgSpeed(this.old);
	}

	public double getHandicappedSpeedAvg(){
		return calculateAvgSpeed(this.handicapped);
	}

	public double getOverallSpeedAvg(){
		return calculateAvgSpeed(this.personList);
	}

	public int getNumberYoungPersons() {
		return this.young.size();
	}


	public int getNumberMidagePersons() {
		return this.midage.size();
	}


	public int getNumberOldPersons() {
		return this.old.size();
	}


	public int getNumberHandicappedPersons() {
		return this.handicapped.size();
	}


	public int getTotalPersons() {
		return this.personList.size();
	}

	public Vector<Person> getPersonList(){ return this.personList;}

}
