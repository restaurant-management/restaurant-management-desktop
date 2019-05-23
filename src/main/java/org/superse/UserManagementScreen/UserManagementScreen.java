package org.superse.UserManagementScreen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class UserManagementScreen extends StackPane {
    public UserManagementScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UserManagementScreen.fxml"));
        this.getChildren().add(root);
    }
}