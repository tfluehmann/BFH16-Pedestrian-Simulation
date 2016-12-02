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

	private Statistic stats;


	public void initialize(URL location, ResourceBundle resources) {
		stats = Statistic.getInstance();
		stats.calculatePersons();

		ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
				new PieChart.Data("Persons under 30 years", stats.getNumberYoungPersons()),
				new PieChart.Data("Persons between 30 and 50 years", stats.getNumberMidagePersons()),
				new PieChart.Data("Persons over 50 years", stats.getNumberOldPersons()),
				new PieChart.Data("Handicapped persons", stats.getNumberHandicappedPersons())
		);
		pieData.forEach(data ->
				data.nameProperty().bind(
						Bindings.concat(
								data.getName(), ": ", data.pieValueProperty()
						)
				)
		);
		personsByType.setData(pieData);

		double val = 0.0;
		double young = 0.0;
		double midage = 0.0;
		double old = 0.0;
		double handicap = 0.0;
		if (stats.getYoungSpeedAvg() > 0.0) young = stats.getYoungSpeedAvg();
		if (stats.getMidageSpeedAvg() > 0.0) midage = stats.getMidageSpeedAvg();
		if (stats.getOldSpeedAvg() > 0.0) old = stats.getOldSpeedAvg();
		if (stats.getHandicappedSpeedAvg() > 0.0) handicap = stats.getHandicappedSpeedAvg();
		BarChart.Series typeValues = new BarChart.Series<>();
		typeValues.getData().add(new BarChart.Data<>("persons under 30 years", young));
		typeValues.getData().add(new BarChart.Data<>("persons between 30 and 50 years", midage));
		typeValues.getData().add(new BarChart.Data<>("persons over 50 years", old));
		typeValues.getData().add(new BarChart.Data<>("handicapped persons", handicap));
		speedByType.getData().addAll(typeValues);

		Vector<Person> personlist = stats.getPersonList();
		ScatterChart.Series personValues = new ScatterChart.Series<>();
		ScatterChart.Series avgValues = new ScatterChart.Series<>();
		int i = 0;
		for (Person p: personlist){
			personValues.getData().add(new ScatterChart.Data<>(""+ (++i), p.getSpeed()/p.getTime()));
			avgValues.getData().add(new ScatterChart.Data<>(""+i, stats.getOverallSpeedAvg()));
		}

		speedByPerson.getData().addAll(personValues, avgValues);
	}

}
