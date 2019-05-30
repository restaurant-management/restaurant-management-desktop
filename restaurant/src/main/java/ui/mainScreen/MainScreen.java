package ui.mainScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainScreen extends StackPane {
    public MainScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        this.getChildren().add(root);
    }
}