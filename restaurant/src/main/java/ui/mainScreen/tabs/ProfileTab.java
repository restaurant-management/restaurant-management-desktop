package ui.mainScreen.tabs;

import bus.UserProfileBus;
import com.jfoenix.controls.JFXButton;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
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
import ui.mainScreen.popups.EditProfilePopup;
import util.CustomDialog;
import util.ErrorDialog;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;


@ViewController(value = "/ui/mainScreen/tabs/ProfileTab.fxml")
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

    @PostConstruct
    void init() {
        UserModel currentUser = new UserProfileBus().getCurrentUser();

        //region avatar
        avatar.setStroke(Color.SEAGREEN);
        String url = currentUser.get_avatar() != null ? currentUser.get_avatar() : "/ui/images/default-avatar.jpg";
        Image im = new Image(url, false);
        avatar.setFill(new ImagePattern(im));
        avatar.setEffect(new DropShadow(+25d, 0d, +2d, Color.valueOf("#9c27b0")));

        cover.setFill(new ImagePattern(new Image("/images/cover.jpg")));
        //endregion

        //region fullName
        bigFullName.setText(currentUser.get_fullName() != null ? currentUser.get_fullName() : currentUser.get_username());
        fullName.setText(currentUser.get_fullName() != null ? currentUser.get_fullName() : "");
        //endregion

        username.setText(currentUser.get_username());
        email.setText(currentUser.get_email());
        role.setText(currentUser.get_role());

        birthday.setText(currentUser.get_birthday() != null ?
                new SimpleDateFormat("dd/MM/yyyy").format(currentUser.get_birthday()) : "");
    }

    @FXML
    @ActionMethod("editAction")
    void editAction() {
        try {
            new CustomDialog("Sửa thông tin", new Flow(EditProfilePopup.class).start(), true).show();
        } catch (FlowException e) {
            new ErrorDialog("Lỗi tạo popup", e.getMessage(), "đóng").show();
        }
    }
}
