package ui.signInScreen;

import bus.AuthenticationBus;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import dao.exceptions.AuthenticationFailException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.accountScreen.AccountScreen;
import util.ErrorDialog;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public JFXTextField textUsername;

    @FXML
    public JFXPasswordField textPassword;

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

    public void loginAction(ActionEvent event) throws IOException {
        if (!textUsername.validate() && !textPassword.validate()) return;

        AuthenticationBus authenticationBus = new AuthenticationBus();
        try {
            authenticationBus.login(textUsername.getText(), textPassword.getText());
            Node node = (Node) event.getSource();
            Stage dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            Scene scene = new Scene(new AccountScreen());
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (AuthenticationFailException e) {
            new ErrorDialog("Đăng nhập thất bại", e.getMessage()).show();
        }
    }
}
