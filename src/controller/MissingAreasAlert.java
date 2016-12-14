package controller;

import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;

/**
 * Created by tgdflto1 on 14/12/16.
 */
public class MissingAreasAlert extends Alert {

    public MissingAreasAlert(Pane basePane) {
        super(AlertType.ERROR);
        this.setTitle("Check your stuff");
        this.setTitle("Check your stuff");
        this.setHeaderText("No curse words please.");
        this.setContentText("You have not created at least one spawn and one goal area.\nPress right click to create them.");
        this.initOwner(basePane.getScene().getWindow());
        this.getDialogPane().getStylesheets().add(getClass().getResource("/view/default.css").toExternalForm());
        this.getDialogPane().getStyleClass().add("myDialog");
        this.showAndWait();
    }

}
