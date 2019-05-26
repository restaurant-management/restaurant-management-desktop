package ui.splashScreen;

import bus.AuthenticationBus;
import com.jfoenix.controls.JFXProgressBar;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ui.accountScreen.AccountScreen;
import ui.base.StageManager;
import ui.signInScreen.SignInScreen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public ImageView logo;

    @FXML
    public JFXProgressBar progressBar;
    private EventHandler<ActionEvent> onFinishAnimation = event -> {
        if (new AuthenticationBus().checkLoggedIn()) {
            try {
                StageManager.getInstance().pushReplacement(new Scene(new AccountScreen()), "Quản lý quán cơm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                StageManager.getInstance().pushReplacement(new Scene(new SignInScreen()), "Quản lý quán cơm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ScaleTransition transition = new ScaleTransition(Duration.seconds(1), logo);

        transition.setFromX(0);
        transition.setFromY(0);
        transition.setToX(1);
        transition.setToY(1);

        transition.play();
        transition.setOnFinished(event -> {
            FadeTransition transition1 = new FadeTransition(Duration.seconds(1), progressBar);
            progressBar.setVisible(true);
            transition1.setFromValue(0);
            transition1.setToValue(1);
            transition1.play();
            transition1.setOnFinished(onFinishAnimation);
        });
    }
}
