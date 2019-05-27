import com.jfoenix.svg.SVGGlyphLoader;
import io.datafx.controller.flow.FlowException;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ui.base.StageManager;
import ui.splashScreen.SplashScreen;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FlowException {

        //region Load font
        new Thread(() -> {
            try {
                SVGGlyphLoader.loadGlyphsFont(MainApp.class.getResourceAsStream("/fonts/icomoon.svg"),
                        "icomoon.svg");
            } catch (IOException ioExc) {
                ioExc.printStackTrace();
            }
        }).start();
        //endregion

        double width = 800;
        double height = 600;
        try {
            Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
            width = bounds.getWidth() / 1.5;
            height = bounds.getHeight() / 1.5;
        } catch (Exception ignored) {
        }
        StageManager.getInstance().push(SplashScreen.class, width, height, "Phần mềm quản lý quán cơm", null);
    }
}
