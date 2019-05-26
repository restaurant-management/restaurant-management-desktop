package ui.splashScreen;

import bus.AppStartedBus;
import bus.AuthenticationBus;
import com.jfoenix.controls.JFXProgressBar;
import dao.exceptions.FetchUserFailException;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ui.base.StageManager;
import ui.mainScreen.MainScreen;
import ui.signInScreen.SignInScreen;
import util.ErrorDialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public ImageView logo;

    @FXML
    public JFXProgressBar progressBar;

    private EventHandler<ActionEvent> onFinishAnimation = event -> appStarted();

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

    private void appStarted() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new AppStartedBus().initializeLogic();
                return null;
            }

            @Override
            protected void succeeded() {
                if (new AuthenticationBus().checkLoggedIn()) {
                    try {
                        StageManager.getInstance().pushReplacement(new Scene(new MainScreen()), "Quản lý quán cơm");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        StageManager.getInstance().pushReplacement(new Scene(new SignInScreen()), "Đăng nhập");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void failed() {
                String title;
                if (getException() instanceof FetchUserFailException) {
                    title = "Lỗi tải thông tin người dùng";
                } else {
                    title = "Lỗi khởi tạo ứng dụng";
                }
                ErrorDialog dialog = new ErrorDialog(title, getException().getMessage(), null);
                dialog.setOnDialogClosed(event -> appStarted());
                dialog.show();
            }
        };

        new Thread(task).start();
    }
}
