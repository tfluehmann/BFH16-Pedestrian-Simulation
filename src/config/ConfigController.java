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
		System.out.println("CONFIG Controller");

		buttonOK.setOnAction((event) -> {
			System.out.println("started simulation");
			buttonOK.getScene().getWindow().hide();
		});
	}

	private TextField doubleToTextfield(Double value) {
		return new TextField("" + value);
	}

	private TextField intToTextfield(Integer value) {
		return new TextField("" + value);
	}

	private Double textFieldToDouble(TextField text) {
		return new Double(text.getText());
	}

	private Integer textFieldToInteger(TextField text) {
		return new Integer(text.getText());
	}

	public TextField getRoomWidth() {
		return this.roomWidth;
	}

	public void setRoomWidth(TextField roomWidth) {
		this.roomWidth = roomWidth;
	}

	public TextField getRoomHeight() {
		return this.roomHeight;
	}

	public void setRoomHeight(TextField roomHeight) {
		this.roomHeight = roomHeight;
	}

	public Slider getSliderYoung() {
		return this.sliderYoung;
	}

	public void setSliderYoung(Slider sliderYoung) {
		this.sliderYoung = sliderYoung;
	}

	public Slider getSliderMidage() {
		return this.sliderMidage;
	}

	public void setSliderMidage(Slider sliderMidage) {
		this.sliderMidage = sliderMidage;
	}

	public Slider getSliderOld() {
		return this.sliderOld;
	}

	public void setSliderOld(Slider sliderOld) {
		this.sliderOld = sliderOld;
	}

	public Slider getSliderHandycap() {
		return this.sliderHandycap;
	}

	public void setSliderHandycap(Slider sliderHandycap) {
		this.sliderHandycap = sliderHandycap;
	}

	public TextField getWeightYoung() {
		return this.weightYoung;
	}

	public void setWeightYoung(TextField weightYoung) {
		this.weightYoung = weightYoung;
	}

	public TextField getWeightMidage() {
		return this.weightMidage;
	}

	public void setWeightMidage(TextField weightMidage) {
		this.weightMidage = weightMidage;
	}

	public TextField getWeightOld() {
		return this.weightOld;
	}

	public void setWeightOld(TextField weightOld) {
		this.weightOld = weightOld;
	}

	public TextField getWeightHandycap() {
		return this.weightHandycap;
	}

	public void setWeightHandycap(TextField weightHandycap) {
		this.weightHandycap = weightHandycap;
	}


}
