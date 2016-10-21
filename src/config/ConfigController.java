package config;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

/**
 * Created by suter1 on 07.10.2016.
 */
public class ConfigController implements Initializable {

	@FXML
	private TextField totalPersons;

	@FXML
	private CheckBox isWeighted;

	@FXML
	private Slider sliderYoung;

	@FXML
	private Slider sliderMidage;

	@FXML
	private Slider sliderOld;

	@FXML
	private Slider sliderHandicap;

	@FXML
	private TextField weightYoung;

	@FXML
	private TextField weightMidage;

	@FXML
	private TextField weightOld;

	@FXML
	private TextField weightHandicap;

	@FXML
	private Button spawnButton;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ConfigModel cfg = ConfigModel.getInstance();

		weightYoung.textProperty().bindBidirectional(sliderYoung.valueProperty(), NumberFormat.getNumberInstance());
		weightMidage.textProperty().bindBidirectional(sliderMidage.valueProperty(), NumberFormat.getNumberInstance());
		weightOld.textProperty().bindBidirectional(sliderOld.valueProperty(), NumberFormat.getNumberInstance());
		weightHandicap.textProperty().bindBidirectional(sliderHandicap.valueProperty(), NumberFormat.getNumberInstance());

//		Accept only int for the sliders. And check that the sum of all Sliders is 100%.
		sliderYoung.valueProperty().addListener((observable, oldValue, newValue) -> {
			sliderYoung.setValue(Math.round(newValue.doubleValue()));
			calculateSlider();
		});

		sliderMidage.valueProperty().addListener((observable, oldValue, newValue) -> {
			sliderMidage.setValue(Math.round(newValue.doubleValue()));
			calculateSlider();
		});

		sliderOld.valueProperty().addListener((observable, oldValue, newValue) -> {
			sliderOld.setValue(Math.round(newValue.doubleValue()));
			calculateSlider();
		});

		sliderHandicap.valueProperty().addListener((observable, oldValue, newValue) -> {
			sliderHandicap.setValue(Math.round(newValue.doubleValue()));
			calculateSlider();
		});

		/**
		 * Numberlistener from user "javasuns"
		 * on: http://stackoverflow.com/a/37360657
		 */
		ChangeListener<String> forceNumberListener = (observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*"))
				((StringProperty) observable).set(oldValue);
		};

//		Make sure, that only numbers (int) are entered in Textfields.
		weightYoung.textProperty().addListener(forceNumberListener);
		weightMidage.textProperty().addListener(forceNumberListener);
		weightOld.textProperty().addListener(forceNumberListener);
		weightHandicap.textProperty().addListener(forceNumberListener);


		spawnButton.setOnAction((event) -> {
			/**
			 * Save data form the config window for usage in simulation.
			 */
			cfg.setTotalPersons(getTotalPersons());
			cfg.setWeighted(getIsWeighted());
			cfg.setWeightedYoungPersons(getWeightYoung());
			cfg.setWeigthedMidagePersons(getWeightMidage());
			cfg.setWeightedOldPersons(getWeightOld());
			cfg.setWeightedHandicappedPersons(getWeightHandicap());



			spawnButton.setDisable(true);

			/**
			 * Merge meter from the config to pixel for the view.
			 */
//			cfg.calculateRoomSize();
		});
	}


	private int sliderSum() {
		return (int) Math.round(sliderYoung.getValue() + sliderMidage.getValue() + sliderOld.getValue() + sliderHandicap.getValue());
	}


	private void calculateSlider() {
		int sum = sliderSum();
		if (sum != 100) {
			int diff = sum - 100;
			sliderYoung.setValue(Math.round(sliderYoung.getValue() - diff / 4));
			sliderMidage.setValue(Math.round(sliderMidage.getValue() - diff / 4));
			sliderOld.setValue(Math.round(sliderOld.getValue() - diff / 4));
			sliderHandicap.setValue(Math.round(sliderHandicap.getValue() - diff / 4));
		}
	}


	public Boolean getIsWeighted() {
		return this.isWeighted.isSelected();
	}


	private TextField doubleToTextfield(Double value) {
		return new TextField("" + value);
	}


	private double textFieldToDouble(TextField text) {
		return new Double(text.getText());
	}


	private int textFieldToInt(TextField text) {
		return new Integer(text.getText());
	}


	private TextField intToTextfield(int value) {
		return new TextField("" + value);
	}


	public double getTotalPersons() {
		return textFieldToDouble(this.totalPersons);
	}


	public void setTotalPersons(double totalPersons) {
		this.totalPersons = doubleToTextfield(totalPersons);
	}


	public double getSliderYoungValue() {
		return this.sliderYoung.getValue();
	}


	public void setSliderYoungValue(double sliderYoung) {
		this.sliderYoung.setValue(sliderYoung);
	}


	public double getSliderMidageValue() {
		return this.sliderMidage.getValue();
	}


	public void setSliderMidageValue(double sliderMidage) {
		this.sliderMidage.setValue(sliderMidage);
	}


	public double getSliderOldValue() {
		return this.sliderOld.getValue();
	}


	public void setSliderOldValue(double sliderOld) {
		this.sliderOld.setValue(sliderOld);
	}


	public double getSliderHandicapValue() {
		return this.sliderHandicap.getValue();
	}


	public void setSliderHandicapValue(double sliderHandicap) {
		this.sliderHandicap.setValue(sliderHandicap);
	}


	public double getWeightYoung() {
		return textFieldToInt(this.weightYoung);
	}


	public void setWeightYoung(int weightYoung) {
		this.weightYoung = intToTextfield(weightYoung);
	}


	public int getWeightMidage() {
		return textFieldToInt(this.weightMidage);
	}


	public void setWeightMidage(int weightMidage) {
		this.weightMidage = intToTextfield(weightMidage);
	}


	public int getWeightOld() {
		return textFieldToInt(this.weightOld);
	}


	public void setWeightOld(int weightOld) {
		this.weightOld = intToTextfield(weightOld);
	}


	public int getWeightHandicap() {
		return textFieldToInt(this.weightHandicap);
	}


	public void setWeightHandicap(int weightHandicap) {
		this.weightHandicap = intToTextfield(weightHandicap);
	}

}