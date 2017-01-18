package controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.layout.AnchorPane;
import model.Statistic;
import model.persons.Person;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Created by suter1 on 25.11.2016.
 * Controller for the statistic window
 */
public class StatisticController implements Initializable {

	@FXML
	private PieChart personsByType;

	@FXML
	private BarChart speedByType;

	@FXML
	private ScatterChart speedByPerson;

	@FXML
	private AnchorPane paneSpeedByType;

    final static String YOUNG_PERSONS = "Persons under 30 years";
    final static String MIDAGE_PERSONS = "Persons between 30 and 50 years";
    final static String OLD_PERSONS = "Persons over 50 years";
    final static String HANDICAPPED_PERSONS = "Handicapped persons";



	public void initialize(URL location, ResourceBundle resources) {
		Statistic stats = Statistic.getInstance();
		stats.calculatePersons();

		ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data(YOUNG_PERSONS, stats.getNumberYoungPersons()),
                new PieChart.Data(MIDAGE_PERSONS, stats.getNumberMidagePersons()),
                new PieChart.Data(OLD_PERSONS, stats.getNumberOldPersons()),
                new PieChart.Data(HANDICAPPED_PERSONS, stats.getNumberHandicappedPersons())
        );
		pieData.forEach(data ->
				data.nameProperty().bind(
						Bindings.concat(
								data.getName(), ": ", data.pieValueProperty()
						)
				)
		);
		personsByType.setData(pieData);

		double young = 0.0;
		double midage = 0.0;
		double old = 0.0;
		double handicap = 0.0;
		double youngStat = stats.getYoungSpeedAvg();
		double midageStat = stats.getMidageSpeedAvg();
		double oldStat = stats.getOldSpeedAvg();
		double handicapStat = stats.getHandicappedSpeedAvg();

		if (youngStat > 0.0) young = youngStat;
		if (midageStat > 0.0) midage = midageStat;
		if (oldStat > 0.0) old = oldStat;
		if (handicapStat > 0.0) handicap = handicapStat;
        BarChart.Series<BarChart, Double> typeValues = new BarChart.Series<>();
        typeValues.getData().add(new BarChart.Data(YOUNG_PERSONS, young));
        typeValues.getData().add(new BarChart.Data(MIDAGE_PERSONS, midage));
        typeValues.getData().add(new BarChart.Data(OLD_PERSONS, old));
        typeValues.getData().add(new BarChart.Data(HANDICAPPED_PERSONS, handicap));
        typeValues.setName("average speed by group of persontype");
		speedByType.getYAxis().setLabel("speed [m/s]");
		speedByType.getData().addAll(typeValues);

		Vector<Person> personlist = stats.getPersonList();
		ScatterChart.Series personValues = new ScatterChart.Series<>();
		ScatterChart.Series avgValues = new ScatterChart.Series<>();
		int i = 0;
		for (Person p : personlist) {
			personValues.getData().add(new ScatterChart.Data<>("" + (++i), stats.getPersonSpeedAvg(p)));
			avgValues.getData().add(new ScatterChart.Data<>("" + i, stats.getOverallSpeedAvg()));
		}
		personValues.setName("average speed by person");
		avgValues.setName("overall average speed of all persons");
		speedByPerson.getYAxis().setLabel("speed [m/s]");
		speedByPerson.getData().addAll(personValues, avgValues);
	}

}
