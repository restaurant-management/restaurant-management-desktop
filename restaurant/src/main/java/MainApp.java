import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.base.StageManager;
import ui.createDishScreen.CreateDishScreen;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {

        Scene scene = new Scene(new CreateDishScreen());

        primaryStage.setTitle("Quản lý quán cơm");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        StageManager.getInstance().push(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
