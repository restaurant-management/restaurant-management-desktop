package ui.dishManagementScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class DishManagemnetScreen extends StackPane {
    public DishManagemnetScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CreateDishScreen.fxml"));
        this.getChildren().add(root);
    }
}