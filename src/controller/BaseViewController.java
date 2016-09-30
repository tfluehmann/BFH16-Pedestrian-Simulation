package controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by tgdflto1 on 30/09/16.
 */
public class BaseViewController implements Initializable{

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        System.out.print("hell");

        // initialize your logic here: all @FXML variables will have been injected

    }

}
