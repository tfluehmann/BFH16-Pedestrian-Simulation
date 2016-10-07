package controller;

import config.ConfigController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Room;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ConfigController.class.getResource("view/Config.fxml"));
			AnchorPane rootLayout;
			rootLayout = loader.load();
			Scene scene = new Scene(rootLayout);

			Stage primaryStage = new Stage();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Config");
			primaryStage.show();
		} catch (Exception ex) {
			Logger.getLogger(ConfigController.class.getName()).log(Level.SEVERE, null, ex);
		}


        System.out.print("hell");
        startButton.setOnAction((event) -> {
            System.out.println("started simulation");
            simulationRoom.start(time);
        });
        // initialize your logic here: all @FXML variables will have been injected

    }


}
