package ui.accountScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class AccountScreen extends StackPane {
    public AccountScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("accountScreen.fxml"));
        this.getChildren().add(root);
    }
}
