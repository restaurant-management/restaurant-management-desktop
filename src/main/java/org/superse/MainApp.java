package org.superse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.superse.AccountScreen.AccountScreen;
import org.superse.SignInScreen.SignInScreen;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(new AccountScreen());

        primaryStage.setTitle("JavaFX and Gradle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
