
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Simulation extends Application {

private AnchorPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Simulation.class.getResource("view/BaseView.fxml"));
			rootLayout = loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Test");
			primaryStage.show();
		} catch (Exception ex) {
			Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
