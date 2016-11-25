package model;

/**
 * Created by suter1 on 25.11.2016.
 */
public class Statistic {

	private static Statistic instance = null;

	private Statistic(){
	}

	public static Statistic getInstance(){
		if (instance == null) {
			instance = new Statistic();
		}
		return instance;
	}

}
