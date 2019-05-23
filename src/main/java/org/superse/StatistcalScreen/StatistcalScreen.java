package org.superse.StatistcalScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class StatistcalScreen extends StackPane {
    public StatistcalScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StatistcalScreen.fxml"));
        this.getChildren().add(root);
    }
}