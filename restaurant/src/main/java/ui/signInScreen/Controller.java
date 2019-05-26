package ui.signInScreen;

import bus.AuthenticationBus;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import ui.accountScreen.AccountScreen;
import ui.base.StageManager;
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
        _loadingDialog = new LoadingDialog("Đang đăng nhập");
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
                _errorDialog = new ErrorDialog("Đăng nhập thất bại", getException().getMessage(), null);
                _errorDialog.show();
                loginFail();
            }
        };

        new Thread(task).start();
    }

    private void loginSuccess() {
        try {
            StageManager.getInstance().pushReplacement(new Scene(new AccountScreen()), "Quản lý quán cơm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginFail() {
        loginButton.setDisable(false);
    }
}
