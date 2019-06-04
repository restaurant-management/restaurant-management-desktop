package ui.mainScreen.tabs.profileTab.popups;


import bus.AuthorizationBus;
import bus.RoleBus;
import bus.UserProfileBus;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.RoleModel;
import model.UserModel;
import model.enums.Permission;
import ui.base.Popupable;
import ui.base.StageManager;
import ui.compenents.ErrorDialog;
import ui.compenents.LoadingDialog;
import ui.compenents.LoadingImage;
import ui.compenents.PrimaryButton;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@ViewController(value = "/ui/mainScreen/tabs/profileTab/popups/EditProfilePopup.fxml")
public class EditProfilePopup extends Popupable {
    private ArrayList<RoleModel> listRoles;
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXTextField fullNameField;
    @FXML
    private JFXDatePicker birthdayField;
    @FXML
    private JFXComboBox<RoleModel> roleField;
    @FXML
    private ImageView avatarField;
    @FXML
    private PrimaryButton submitButton;

    private UserModel _user;
    private UserProfileBus _userProfileBus = new UserProfileBus();
    private AuthorizationBus _authorizationBus = new AuthorizationBus();

    @Override
    protected void get() {
        _user = (UserModel) context.getRegisteredObject("Object0");
    }

    @PostConstruct
    void init() {
        Objects.requireNonNull(context, "context");
        get();

        if (!_authorizationBus.checkPermission(Permission.USER_MANAGEMENT)) {
            roleField.setPromptText(_user.get_role());
            roleField.setDisable(true);
        } else loadRole();

        emailField.setText(_user.get_email());
        fullNameField.setText(_user.get_fullName());
        if (_user.get_birthday() != null)
            birthdayField.setValue(_user.get_birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        submitButton.setOnAction(event -> Platform.runLater(this::commitChange));
        if (_user.get_avatar() != null) {
            new LoadingImage(_user.get_avatar(), "/images/default-avatar.jpg").start(avatarField);
        }


        final FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        avatarField.setOnMouseClicked(event -> {
            File file = fileChooser.showOpenDialog(StageManager.getInstance().getCurrent());
            if (file == null) return;
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                avatarField.setImage(image);
                _user.set_avatar(file.getPath());
            } catch (IOException e) {
                new ErrorDialog("Lỗi tải hình", e.getMessage()).show();
            }
        });
    }

    private void commitChange() {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang cập nhật").show();
        Task<Void> commitChangeTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (emailField.getText() != null && !emailField.getText().isEmpty())
                    _user.set_email(emailField.getText());
                if (fullNameField.getText() != null && !fullNameField.getText().isEmpty())
                    _user.set_fullName(fullNameField.getText());
                if (birthdayField.getValue() != null)
                    _user.set_birthday(Date.from(birthdayField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                if (roleField.getValue() != null)
                    _user.set_role(roleField.getValue().get_slug());
                _userProfileBus.updateProfile(_user);
                if (_authorizationBus.checkPermission(Permission.USER_MANAGEMENT))
                    _userProfileBus.changeRole(_user);

                _userProfileBus.updateCurrentUser();
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi cập nhật", getException().getMessage()).show();
            }
        };
        new Thread(commitChangeTask).start();
    }

    private void loadRole() {
        Task<Void> loadRoleTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                roleField.setPromptText("Đang tải");
                listRoles = RoleBus.get_instance().getListRoles();
                return null;
            }

            @Override
            protected void succeeded() {
                roleField.setPromptText("Vai trò");
                roleField.setItems(FXCollections.observableArrayList(listRoles));
                for (RoleModel role : listRoles) {
                    if (role.get_slug().equals(_user.get_role()))
                        roleField.setValue(role);
                }
            }

            @Override
            protected void failed() {
                new ErrorDialog("Lỗi tải danh sách vai trò", getException().getMessage()).show();
            }
        };
        new Thread(loadRoleTask).start();
    }
}
