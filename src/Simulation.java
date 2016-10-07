
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Simulation extends Application {

	private AnchorPane rootLayout;
	private Scene scene, configScene;
	private Stage baseViewStage, configStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();

			FXMLLoader configLoader = new FXMLLoader();
			configLoader.setLocation(Simulation.class.getResource("view/Config.fxml"));
			configScene = new Scene(configLoader.load());

			loader.setLocation(Simulation.class.getResource("view/BaseView.fxml"));
			rootLayout = loader.load();
			scene = new Scene(rootLayout);

			configStage = new Stage();
			configStage.setScene(configScene);
			configStage.initModality(Modality.APPLICATION_MODAL);
			configStage.setTitle("Configuration");
			configStage.showAndWait();

			primaryStage.setScene(scene);
			primaryStage.setTitle("Test");
			primaryStage.show();
		} catch (Exception ex) {
			Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
