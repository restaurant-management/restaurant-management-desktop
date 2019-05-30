package ui.mainScreen.tabs.profileTab;

import bus.UserProfileBus;
import com.jfoenix.controls.JFXButton;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.action.*;
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
import ui.mainScreen.tabs.profileTab.popups.EditProfilePopup;
import util.CustomDialog;
import util.ErrorDialog;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;


@ViewController(value = "/ui/mainScreen/tabs/profileTab/ProfileTab.fxml")
public class ProfileTab {
    @FXML
    @ActionTrigger("editAction")
    private JFXButton editButton;
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

    private UserModel _currentUser = new UserProfileBus().getCurrentUser();

    @PostConstruct
    void init() {

        //region avatar
        avatar.setStroke(Color.SEAGREEN);
        String url = _currentUser.get_avatar() != null ? _currentUser.get_avatar() : "/ui/images/default-avatar.jpg";
        Image im = new Image(url, false);
        avatar.setFill(new ImagePattern(im));
        avatar.setEffect(new DropShadow(+25d, 0d, +2d, Color.valueOf("#9c27b0")));

        cover.setFill(new ImagePattern(new Image("/images/cover.jpg")));
        //endregion

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

    @FXML
    @ActionMethod("editAction")
    void editAction() {
        try {
            new CustomDialog("Sửa thông tin", EditProfilePopup.create(_currentUser), true).show();
        } catch (FlowException e) {
            new ErrorDialog("Lỗi tạo popup", e.getMessage(), "đóng").show();
        }
    }
}
