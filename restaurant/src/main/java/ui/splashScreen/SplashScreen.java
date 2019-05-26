package ui.splashScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class SplashScreen extends StackPane {
    public SplashScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
        this.getChildren().add(root);
    }
}
