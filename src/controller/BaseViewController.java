package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Room;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by tgdflto1 on 30/09/16.
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
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        System.out.print("hell");
        startButton.setOnAction((event) -> {
            System.out.println("started simulation");
            simulationRoom.start(time);
        });
        // initialize your logic here: all @FXML variables will have been injected

    }


}
