package model;

/**
 * Created by suter1 on 25.11.2016.
 */
public class Statistic {

	private static Statistic instance = null;
	private int numberYoungPersons;
	private int numberMidagePersons;
	private int numberOldPersons;
	private int numberHandicappedPersons;

	private Statistic(){
	}

	public static Statistic getInstance(){
		if (instance == null) {
			instance = new Statistic();
		}
		return instance;
	}


	public void setNumberYoungPersons(int numberYoungPersons) {
		this.numberYoungPersons = numberYoungPersons;
	}


	public void setNumberMidagePersons(int numberMidagePersons) {
		this.numberMidagePersons = numberMidagePersons;
	}


	public void setNumberOldPersons(int numberOldPersons) {
		this.numberOldPersons = numberOldPersons;
	}


	public void setNumberHandicappedPersons(int numberHandicappedPersons) {
		this.numberHandicappedPersons = numberHandicappedPersons;
	}

}
