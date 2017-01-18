package controller;

import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;

/**
 * Created by suter1 on 18.01.2017.
 *  alert in case if the person spawn should be weighted and the distribution-sum is not equal 100%.
 */
public class SliderAlert extends Alert {

	public SliderAlert(Pane basePane) {
		super(AlertType.ERROR);
		this.setTitle("Check your stuff");
		this.setTitle("Check your stuff");
		this.setHeaderText("Check your person distribution.");
		this.setContentText("Your person distribution isn't on 100%.\nPlease fix them or deselect the checkbox before spawn persons.");
		this.initOwner(basePane.getScene().getWindow());
		this.getDialogPane().getStylesheets().add(getClass().getResource("/view/default.css").toExternalForm());
		this.getDialogPane().getStyleClass().add("myDialog");
		this.showAndWait();
	}
}
