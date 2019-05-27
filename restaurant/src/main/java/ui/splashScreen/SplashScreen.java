package ui.splashScreen;

import bus.AppStartedBus;
import bus.AuthenticationBus;
import com.jfoenix.controls.JFXProgressBar;
import dao.exceptions.FetchUserFailException;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.util.VetoException;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ui.mainScreen.MainScreen;
import ui.signInScreen.SignInScreen;
import util.ErrorDialog;

import javax.annotation.PostConstruct;

@ViewController(value = "/ui/splashScreen/SplashScreen.fxml", title = "Phần mềm quản lý quán cơm Ver 1.0")
public class SplashScreen {
    @FXML
    private ImageView logo;

    @FXML
    private JFXProgressBar progressBar;
    @ActionHandler
    private FlowActionHandler actionHandler;
    private EventHandler<ActionEvent> onFinishAnimation = event -> appStarted();

    @PostConstruct
    public void init() {
        ScaleTransition transition = new ScaleTransition(Duration.seconds(2), logo);

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
                    // TODO navigate to main screen
                    try {
                        actionHandler.navigate(MainScreen.class);
                    } catch (VetoException | FlowException e) {
                        e.printStackTrace();
                    }
                } else {
                    // TODO navigate to sign in screen
                    try {
                        actionHandler.navigate(SignInScreen.class);
                    } catch (VetoException | FlowException e) {
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
