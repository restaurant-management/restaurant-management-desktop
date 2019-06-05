package ui.mainScreen.tabs.profileTab;

import bus.UserProfileBus;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.UserModel;
import ui.compenents.*;
import ui.mainScreen.tabs.profileTab.popups.ChangePasswordPopup;
import ui.mainScreen.tabs.profileTab.popups.EditProfilePopup;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;


@ViewController(value = "/ui/mainScreen/tabs/profileTab/ProfileTab.fxml")
public class ProfileTab {
    @FXML
    @ActionTrigger("editAction")
    private JFXButton editButton;
    @FXML
    @ActionTrigger("changePassAction")
    private JFXButton changePassButton;
    @FXML
    private Circle avatar;
    @FXML
    private Rectangle cover;
    @FXML
    private VBox profileBox;
    @FXML
    private Label bigFullName;
    @FXML
    private Label fullName;
    @FXML
    private Label username;
    @FXML
    private Label email;
    @FXML
    private Label role;
    @FXML
    private Label birthday;
//    @FXML
//    private IconButton reloadButton;

    private UserModel _currentUser = new UserProfileBus().getCurrentUser();

    @PostConstruct
    void init() {
//        reloadButton.setIcon(FontAwesomeIcon.UNDO, null);
//        reloadButton.setOnAction(event -> fetchCurrentUser());

        cover.setFill(new ImagePattern(new Image("/images/cover.jpg")));

        initData();
    }

    private void initData() {
        avatar.setStroke(Color.SEAGREEN);
        String url = _currentUser.get_avatar() != null ? _currentUser.get_avatar() : "/images/default-avatar.jpg";
        new LoadingImage(url, "/images/default-avatar.jpg").start(avatar);
        avatar.setEffect(new DropShadow(+25d, 0d, +2d, Color.valueOf("#9c27b0")));

        //region fullName
        bigFullName.setText(_currentUser.get_fullName() != null ? _currentUser.get_fullName() : _currentUser.get_username());
        fullName.setText(_currentUser.get_fullName() != null ? _currentUser.get_fullName() : "");
        //endregion

        username.setText(_currentUser.get_username());
        email.setText(_currentUser.get_email());
        role.setText(_currentUser.get_role());

        birthday.setText(_currentUser.get_birthday() != null ?
                new SimpleDateFormat("dd/MM/yyyy").format(_currentUser.get_birthday()) : "");
    }

    private void fetchCurrentUser() {
        UserProfileBus bus = new UserProfileBus();
        LoadingDialog loadingDialog = new LoadingDialog(null).show();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                bus.updateCurrentUser();
                _currentUser = bus.getCurrentUser();
                return null;
            }

            @Override
            protected void succeeded() {
                initData();
                loadingDialog.close();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                loadingDialog.close();
                new ErrorDialog("Lỗi khi cập nhật thông tin", getException().getMessage()).show();
            }
        };
        new Thread(task).start();
    }

    @FXML
    @ActionMethod("editAction")
    void editAction() {
        try {
            new CustomDialog("Sửa thông tin", EditProfilePopup.class, _currentUser)
                    .setOnDialogClosed(event -> initData()).show();
        } catch (FlowException e) {
            e.printStackTrace();
            new ErrorDialog("Lỗi tạo popup", e.getMessage()).show();
        }
    }

    @FXML
    @ActionMethod("changePassAction")
    void changePassAction() {
        try {
            new CustomDialog("Đổi mật khẩu", ChangePasswordPopup.class, _currentUser).show();
        } catch (FlowException e) {
            e.printStackTrace();
            new ErrorDialog("Lỗi tạo popup", e.getMessage()).show();
        }
    }
}
