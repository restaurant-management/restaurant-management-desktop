package ui.signInScreen;

import bus.AuthenticationBus;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.accountScreen.AccountScreen;
import util.ErrorDialog;
import util.LoadingDialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public JFXTextField textUsername;

    @FXML
    public JFXPasswordField textPassword;

    @FXML
    public JFXButton loginButton;

    @FXML
    public JFXProgressBar loadingBar;


    private LoadingDialog _loadingDialog;
    private ErrorDialog _errorDialog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RequiredFieldValidator validator = new RequiredFieldValidator();

        textUsername.getValidators().add(validator);
        textPassword.getValidators().add(validator);

        textUsername.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                textUsername.validate();
        });
        textPassword.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                textPassword.validate();
        });
    }

    @FXML
    void loginAction() {
        if (!textUsername.validate() && !textPassword.validate()) return;
        loginButton.setDisable(true);
        _loadingDialog = new LoadingDialog(textUsername.getScene(), "Đang đăng nhập");
        _loadingDialog.show();
        if (_errorDialog != null) _errorDialog.close();
        login();
    }

    private void login() {
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                AuthenticationBus authenticationBus = new AuthenticationBus();
                authenticationBus.login(textUsername.getText(), textPassword.getText());
                return null;
            }

            @Override
            protected void done() {
                if (_loadingDialog != null) _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                loginSuccess();
            }

            @Override
            protected void failed() {
                _errorDialog = new ErrorDialog(textUsername.getScene(), "Đăng nhập thất bại", getException().getMessage(), null);
                _errorDialog.show();
                loginFail();
            }
        };

//        task.setOnRunning();

        new Thread(task).start();
    }

    private void loginSuccess() {
        Stage stage = (Stage) textUsername.getScene().getWindow();
        stage.close();
        Scene scene = null;
        try {
            scene = new Scene(new AccountScreen());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.show();
    }

    private void loginFail() {
        loginButton.setDisable(false);
    }
}
