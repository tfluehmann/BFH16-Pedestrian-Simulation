package controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import model.Statistic;

import java.net.URL;
import java.util.ResourceBundle;

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

	private Statistic stats;


	public void initialize(URL location, ResourceBundle resources) {
		stats = Statistic.getInstance();
		stats.getPersons();

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

		XYChart.Series<String,Double> avgSpeedByTypeData = new XYChart.Series<>();
		if (stats.getNumberYoungPersons() > 0){
			val = stats.getYoungSpeedAvg();
		} else {
			val = 0.0;
		}
		avgSpeedByTypeData.getData().add(new XYChart.Data<>("persons under 30 years", val));
		if (stats.getNumberMidagePersons() > 0){
			val = stats.getMidageSpeedAvg();
		} else {
			val = 0.0;
		}
		avgSpeedByTypeData.getData().add(new XYChart.Data<>("persons between 30 and 50 years", val));
		if (stats.getNumberOldPersons() > 0){
			val = stats.getOldSpeedAvg();
		} else {
			val = 0.0;
		}
		avgSpeedByTypeData.getData().add(new XYChart.Data<>("persons over 50 years", val));
		if (stats.getNumberHandicappedPersons() > 0){
			val = stats.getHandicappedSpeedAvg();
		} else {
			val = 0.0;
		}
		avgSpeedByTypeData.getData().add(new XYChart.Data<>("handicapped persons", val));

		this.speedByType.legendVisibleProperty().setValue(false);

		this.speedByType.getData().add(avgSpeedByTypeData);
	}

}
