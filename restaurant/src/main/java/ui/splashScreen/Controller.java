package ui.splashScreen;

import com.jfoenix.controls.JFXProgressBar;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public ImageView logo;

    @FXML
    public JFXProgressBar progressBar;

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
        });
    }
}
