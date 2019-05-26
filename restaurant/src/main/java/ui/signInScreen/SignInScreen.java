package ui.signInScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class SignInScreen extends StackPane {
    public SignInScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("signInScreen.fxml"));
        this.getChildren().add(root);
    }
}
