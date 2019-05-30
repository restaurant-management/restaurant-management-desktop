package ui.splashScreen;

import bus.AppStartedBus;
import bus.AuthenticationBus;
import com.jfoenix.controls.JFXProgressBar;
import dao.exceptions.userExceptions.FetchUserFailException;
import exceptions.DontHavePermissionException;
import exceptions.NavigateFailedException;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.util.VetoException;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
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

@ViewController(value = "/ui/splashScreen/SplashScreen.fxml")
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
                    Platform.runLater(() -> {
                        try {
                            actionHandler.navigate(MainScreen.class);
                        } catch (VetoException | FlowException e) {
                            handleError(e);
                        }
                    });
                } else {
                    try {
                        actionHandler.navigate(SignInScreen.class);
                    } catch (VetoException | FlowException e) {
                        handleError(e);
                    }
                }
            }

            @Override
            protected void failed() {
                handleError(getException());
            }
        };
        new Thread(task).start();
    }

    private void logout() {
        try {
            new AuthenticationBus().logout();
        } catch (NavigateFailedException e) {
            ErrorDialog dialog = new ErrorDialog("Lỗi chuyển màn hình", e.getMessage(), null);
            dialog.setOnDialogClosed(event -> logout());
            dialog.show();
        }
    }

    private void handleError(Throwable exception) {

        String title;
        if (exception instanceof DontHavePermissionException) {
            ErrorDialog dialog = new ErrorDialog("Lỗi tài khoản",
                    "Tài khoản không có quyền truy cập. Nhấn đóng để đăng xuất.", null);
            dialog.setOnDialogClosed(event -> logout());
            dialog.show();
            return;
        }
        if (exception instanceof FetchUserFailException) {
            title = "Lỗi tải thông tin người dùng";
        } else {
            title = "Lỗi khởi tạo ứng dụng";
        }
        ErrorDialog dialog = new ErrorDialog(title, exception.getMessage(), null);
        dialog.setOnDialogClosed(event -> appStarted());
        dialog.show();
    }
}
