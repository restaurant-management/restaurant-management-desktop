package ui.signInScreen;

import bus.AuthenticationBus;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.util.VetoException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import ui.mainScreen.MainScreen;
import ui.compenents.ErrorDialog;
import ui.compenents.LoadingDialog;

import javax.annotation.PostConstruct;

@ViewController(value = "/ui/signInScreen/SignInScreen.fxml")
public class SignInScreen{
    @FXML
    private JFXTextField textUsername;

    @FXML
    private JFXPasswordField textPassword;

    @FXML
    @ActionTrigger("loginAction")
    private JFXButton loginButton;

    @ActionHandler
    private FlowActionHandler actionHandler;


    private LoadingDialog _loadingDialog;
    private ErrorDialog _errorDialog;

    @PostConstruct
    public void init() {
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
    @ActionMethod("loginAction")
    void loginAction() {
        if (!textUsername.validate() || !textPassword.validate()) return;
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
                new AuthenticationBus().login(textUsername.getText(), textPassword.getText());
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
                _errorDialog = new ErrorDialog("Đăng nhập thất bại", getException().getMessage());
                _errorDialog.show();
                loginFail();
            }
        };

        new Thread(task).start();
    }

    private void loginSuccess() {
        try {
            actionHandler.navigate(MainScreen.class);
        } catch (VetoException | FlowException e) {
            e.printStackTrace();
        }
    }

    private void loginFail() {
        loginButton.setDisable(false);
    }
}
