import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.signInScreen.SignInScreen;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {

        Scene scene = new Scene(new SignInScreen());

        primaryStage.setTitle("Quản lý quán cơm");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
