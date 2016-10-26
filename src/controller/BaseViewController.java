package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import manager.SpawnManager;
import model.Room;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by fluth1 on 30/09/16.
 */
public class BaseViewController implements Initializable{

    @FXML
    private Room simulationRoom;

    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Label time;

    @FXML
    private Button resetButton;

	@FXML
	private AnchorPane conf;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        startButton.setOnAction((event) -> {
	        simulationRoom.getChildren().addAll(SpawnManager.getInstance().getPersons());
            simulationRoom.start(time);
            startButton.setDisable(true);
            pauseButton.setDisable(false);

        });

        pauseButton.setOnAction((event) -> {
            simulationRoom.getSimulationThread().interrupt();
            pauseButton.setDisable(true);
            startButton.setDisable(false);
        });
        // initialize your logic here: all @FXML variables will have been injected

    }
}
