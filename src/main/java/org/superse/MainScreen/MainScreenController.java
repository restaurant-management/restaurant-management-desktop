package org.superse.MainScreen;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
    }
}
