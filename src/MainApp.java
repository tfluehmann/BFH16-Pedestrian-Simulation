import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainApp extends Application {

	private AnchorPane rootLayout;
	private Scene scene, configScene;
	private Stage configStage;


	public static void main(String[] args) {
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) {
		try {
			// creating view for the initial configuration
			FXMLLoader configLoader = new FXMLLoader();
			configLoader.setLocation(MainApp.class.getResource("view/ConfigWindow.fxml"));
			configScene = new Scene(configLoader.load());
			configStage = new Stage();
			configStage.setScene(configScene);
			configStage.initModality(Modality.APPLICATION_MODAL);
			configStage.setTitle("Configuration");
			configStage.showAndWait();

			// Creating view for the simulation
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/BaseView.fxml"));
			rootLayout = loader.load();
			scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
            primaryStage.setTitle("Pedestrian Simulation");
            primaryStage.show();
        } catch (Exception ex) {
			Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
