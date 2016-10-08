package config;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by suter1 on 07.10.2016.
 */
public class ConfigController implements Initializable {

	@FXML
	private TextField roomWidth;

	@FXML
	private TextField roomHeight;

	@FXML
	private TextField totalPersons;

	@FXML
	private Slider sliderYoung;

	@FXML
	private Slider sliderMidage;

	@FXML
	private Slider sliderOld;

	@FXML
	private Slider sliderHandycap;

	@FXML
	private TextField weightYoung;

	@FXML
	private TextField weightMidage;

	@FXML
	private TextField weightOld;

	@FXML
	private TextField weightHandycap;

	@FXML
	private Button buttonOK;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ConfigModel cfg = ConfigModel.getInstance();
		System.out.println("CONFIG Controller");

		buttonOK.setOnAction((event) -> {
			System.out.println("started simulation");
			cfg.setRoomWidthMeter(getRoomWidth());
			cfg.setRoomHeightMeter(getRoomHeight());
			cfg.setTotalPersons(getTotalPersons());
			cfg.setWeightedYoungPersons(getWeightYoung());
			cfg.setWeigthedMidagePersons(getWeightMidage());
			cfg.setWeightedOldPersons(getWeightOld());
			cfg.setWeightedHandycappedPersons(getWeightHandycap());

			cfg.calculateRoomSize();

			System.out.println("Width meter: " + cfg.getRoomWidthMeter() + ", Height meter: " + cfg.getRoomHeightMeter());
			System.out.println("Room width: " + cfg.getRoomWidth() + ", Room heigt: " + cfg.getRoomHeight());
			System.out.println("Pixel to meter ratio: " + cfg.getPixelPerMeter());
			buttonOK.getScene().getWindow().hide();
		});
	}


	private TextField doubleToTextfield(Double value) {
		return new TextField("" + value);
	}


	private double textFieldToDouble(TextField text) {
		return new Double(text.getText());
	}


	public double getTotalPersons() {
		return textFieldToDouble(this.totalPersons);
	}


	public void setTotalPersons(double totalPersons) {
		this.totalPersons = doubleToTextfield(totalPersons);
	}


	public double getRoomWidth() {
		return textFieldToDouble(this.roomWidth);
	}


	public void setRoomWidth(Double roomWidth) {
		this.roomWidth = doubleToTextfield(roomWidth);
	}


	public double getRoomHeight() {
		return textFieldToDouble(this.roomHeight);
	}


	public void setRoomHeight(double roomHeight) {
		this.roomHeight = doubleToTextfield(roomHeight);
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


	public double getSliderHandycapValue() {
		return this.sliderHandycap.getValue();
	}


	public void setSliderHandycapValue(double sliderHandycap) {
		this.sliderHandycap.setValue(sliderHandycap);
	}


	public double getWeightYoung() {
		return textFieldToDouble(this.weightYoung);
	}


	public void setWeightYoung(double weightYoung) {
		this.weightYoung = doubleToTextfield(weightYoung);
	}


	public double getWeightMidage() {
		return textFieldToDouble(this.weightMidage);
	}


	public void setWeightMidage(double weightMidage) {
		this.weightMidage = doubleToTextfield(weightMidage);
	}


	public double getWeightOld() {
		return textFieldToDouble(this.weightOld);
	}


	public void setWeightOld(double weightOld) {
		this.weightOld = doubleToTextfield(weightOld);
	}


	public double getWeightHandycap() {
		return textFieldToDouble(this.weightHandycap);
	}


	public void setWeightHandycap(double weightHandycap) {
		this.weightHandycap = doubleToTextfield(weightHandycap);
	}

}