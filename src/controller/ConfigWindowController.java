package controller;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.ConfigModel;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by suter1 on 07.10.2016.
 */
public class ConfigWindowController implements Initializable {

	@FXML
	private TextField roomWidth;

	@FXML
	private TextField roomHeight;

	@FXML
	private ComboBox spawnAreaPosition;

	@FXML
	private TextField spawnWidth;

	@FXML
	private TextField spawnHeight;

	@FXML
	private ComboBox goalAreaPosition;

	@FXML
	private TextField goalWidth;

	@FXML
	private TextField goalHeight;

	@FXML
	private Button buttonOK;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ConfigModel cfg = ConfigModel.getInstance();

		/**
		 * Numberlistener from user "javasuns"
		 * on: http://stackoverflow.com/a/37360657
		 */
		ChangeListener<String> forceNumberListener = (observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*"))
				((StringProperty) observable).set(oldValue);
		};

		buttonOK.setOnAction((event) -> {
			/**
			 * Save data form the config window for usage in simulation.
			 */
			cfg.setRoomWidthMeter(getRoomWidth());
			cfg.setRoomHeightMeter(getRoomHeight());

			/**
			 * Merge meter from the config to pixel for the view.
			 */
			cfg.calculateRoomSize();

			/**
			 * Close the config window to start the simulation.
			 */
			buttonOK.getScene().getWindow().hide();
		});
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

}