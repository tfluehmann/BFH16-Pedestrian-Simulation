package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedBarChart;
import manager.SpawnManager;
import model.Statistic;
import model.persons.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Created by suter1 on 25.11.2016.
 */
public class StatisticController implements Initializable {

	@FXML
	private PieChart personsByType;

	@FXML
	private StackedBarChart speedByType;

	@FXML
	private ScatterChart speedByPerson;

	private Statistic stats;
	private int young = 0;
	private int midage = 0;
	private int old = 0;
	private int handicap = 0;


	public void initialize(URL location, ResourceBundle resources) {
		stats = Statistic.getInstance();
		Vector<Person> activePersons = SpawnManager.getInstance().getPersons();
		Vector<Person> passivePersons = SpawnManager.getInstance().getPassivePersons();

		for (Person p : activePersons) {
			definePersonType(p);
		}

		for (Person p : passivePersons) {
			definePersonType(p);
		}

		ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
				new PieChart.Data("Persons under 30 years", young),
				new PieChart.Data("Persons between 30 and 50 years", midage),
				new PieChart.Data("Persons over 50 years", old),
				new PieChart.Data("Handicapped persons", handicap)
		);

		personsByType.setData(pieData);
	}

	private void definePersonType(Person p){

		if (p instanceof YoungPerson) young++;
		else if (p instanceof MidAgePerson) midage++;
		else if (p instanceof OldPerson) old++;
		else if (p instanceof HandicappedPerson) handicap++;
	}

}
