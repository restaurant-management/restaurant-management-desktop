package ui.mainScreen.tabs.profileTab.popups;

import bus.UserProfileBus;
import com.jfoenix.controls.JFXPasswordField;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import model.UserModel;
import ui.base.Popupable;
import ui.compenents.ErrorDialog;
import ui.compenents.LoadingDialog;
import ui.compenents.PrimaryButton;
import ui.compenents.SuccessDialog;

import javax.annotation.PostConstruct;
import java.util.Objects;

@ViewController(value = "/ui/mainScreen/tabs/profileTab/popups/ChangePasswordPopup.fxml")
public class ChangePasswordPopup extends Popupable {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXPasswordField oldPasswordField;
    @FXML
    private JFXPasswordField newPasswordField;
    @FXML
    private JFXPasswordField rePasswordField;
    @FXML
    private PrimaryButton submitButton;

    private UserModel _user;
    private UserProfileBus _userProfileBus = new UserProfileBus();

    @Override
    protected void get() {
        _user = (UserModel) context.getRegisteredObject("Object0");
    }

    @PostConstruct
    private void init() {
        Objects.requireNonNull(context, "context");
        get();

        submitButton.setOnAction(event -> commitChange());
    }

    private void commitChange() {
        if (!rePasswordField.getText().equals(newPasswordField.getText())) {
            new ErrorDialog("Lỗi", "Nhập lại mật khẩu không khớp với mật khẩu mới.").show();
            return;
        }

        if (oldPasswordField.getText().equals(newPasswordField.getText())) {
            new ErrorDialog("Lỗi", "Mật khẩu mới phải khác mật khẩu cũ.").show();
            return;
        }

        if (newPasswordField.getText().length() < 6) {
            new ErrorDialog("Lỗi", "Mật khẩu mới phải lớn hơn 5 ký tự.").show();
            return;
        }

        LoadingDialog _loadingDialog = new LoadingDialog("Đang cập nhật").show();
        Task<Void> commitChangeTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String oldPassword = "";
                String newPassword = "";
                if (oldPasswordField.getText() != null && !oldPasswordField.getText().isEmpty())
                    oldPassword = oldPasswordField.getText();
                if (newPasswordField.getText() != null && !newPasswordField.getText().isEmpty())
                    newPassword = newPasswordField.getText();

                _userProfileBus.changePassword(_user, oldPassword, newPassword);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Thành công", "Sửa mật khẩu thành công").show();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi cập nhật mật khẩu", getException().getMessage()).show();
            }
        };
        new Thread(commitChangeTask).start();
    }
}
