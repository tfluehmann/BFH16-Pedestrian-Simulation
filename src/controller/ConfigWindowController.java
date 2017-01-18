package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.ConfigModel;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by suter1 on 07.10.2016.
 * Initial config window for the room size
 */
public class ConfigWindowController implements Initializable {

	@FXML
	private TextField roomWidth;

	@FXML
	private TextField roomHeight;

	@FXML
	private Button buttonOK;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ConfigModel cfg = ConfigModel.getInstance();


		buttonOK.setOnAction((event) -> {
            /*
			 * Save data form the config window for usage in simulation.
			 */
			cfg.setRoomWidthMeter(getRoomWidth());
			cfg.setRoomHeightMeter(getRoomHeight());

			/*
             * Merge meter from the config to pixel for the view.
			 */
			cfg.calculateRoomSize();

			/*
             * Close the config window to start the simulation.
			 */
			buttonOK.getScene().getWindow().hide();
		});
	}

	private double textFieldToDouble(TextField text) {
		return new Double(text.getText());
	}

    private double getRoomWidth() {
        return textFieldToDouble(this.roomWidth);
	}


    private double getRoomHeight() {
        return textFieldToDouble(this.roomHeight);
	}
}