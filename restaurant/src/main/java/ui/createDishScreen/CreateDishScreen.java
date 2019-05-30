package ui.createDishScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class CreateDishScreen extends StackPane {
    public CreateDishScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CreateDishScreen.fxml"));
        this.getChildren().add(root);
    }
}