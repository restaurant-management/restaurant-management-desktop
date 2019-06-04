package ui.mainScreen.tabs.userTab.popups;

import bus.AuthorizationBus;
import bus.RoleBus;
import bus.UserProfileBus;
import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.RoleModel;
import model.UserModel;
import model.enums.Permission;
import model.exceptions.IsNotAPermissionException;
import ui.base.Popupable;
import ui.base.StageManager;
import ui.compenents.*;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@ViewController(value = "/ui/mainScreen/tabs/userTab/popups/AddUserPopup.fxml")
public class EditUserPopup extends Popupable {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXTextField fullNameField;
    @FXML
    private JFXDatePicker birthdayField;
    @FXML
    private JFXComboBox<RoleModel> roleField;
    @FXML
    private JFXListView<JFXCheckBox> permissionField;
    @FXML
    private ImageView avatarField;
    @FXML
    private PrimaryButton submitButton;
    private ArrayList<RoleModel> listRoles;
    private UserModel _user;

    @Override
    protected void get() {
        String username = (String) context.getRegisteredObject("Object0");
        loadUser(username);
    }

    @PostConstruct
    private void init() {
        Objects.requireNonNull(context, "context");
        get();
        setupPermissionField();
        loadRole();

        submitButton.setText("Cập nhật");
        submitButton.setOnAction(event -> submit());
    }

    private void submit() {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang cập nhật").show();
        Task<Void> commitChangeTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                UserProfileBus _userProfileBus = new UserProfileBus();
                if (emailField.getText() != null && !emailField.getText().isEmpty())
                    _user.set_email(emailField.getText());
                if (fullNameField.getText() != null)
                    _user.set_fullName(fullNameField.getText());
                if (birthdayField.getValue() != null)
                    _user.set_birthday(Date.from(birthdayField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                if (roleField.getValue() != null)
                    _user.set_role(roleField.getValue().get_slug());
                _user.set_permissions(getPermissionsSelected());

                _userProfileBus.updateProfile(_user);
                _userProfileBus.changeRole(_user);
                if(passwordField.getText() != null && !passwordField.getText().isEmpty())
                    _userProfileBus.changePassword(_user, null, passwordField.getText());
                return null;
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Cập nhật thông tin người dùng thành công", "Người dùng đã được cập nhật").show();
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

    private void loadUser(String username) {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang tải").show();
        Task<Void> loadUserTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _user = new UserProfileBus().getUserProfile(username);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                //region setup interface
                usernameField.setDisable(true);
                usernameField.setText(_user.get_username());
                emailField.setText(_user.get_email());
                fullNameField.setText(_user.get_fullName());
                if (_user.get_birthday() != null)
                    birthdayField.setValue(_user.get_birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
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
                //endregion
                if (listRoles != null) {
                    for (RoleModel role : listRoles) {
                        if (role.get_slug().equals(_user.get_role()))
                            roleField.setValue(role);
                    }
                }
                for (JFXCheckBox permissionCheckBox : permissionField.getItems()) {
                    try {
                        if (_user.get_permissions().contains(Permission.get(permissionCheckBox.getText()))) {
                            permissionCheckBox.setSelected(true);
                        }
                    } catch (IsNotAPermissionException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi tải thông tin", getException().getMessage()).show();
            }
        };
        new Thread(loadUserTask).start();
    }

    private void setupPermissionField() {
        ObservableList<JFXCheckBox> list = FXCollections.observableArrayList();
        list.add(new JFXCheckBox(Permission.CREATE_BILL.toString()));
        list.add(new JFXCheckBox(Permission.UPDATE_COMPLETE_BILL_STATUS.toString()));
        list.add(new JFXCheckBox(Permission.UPDATE_DELIVERING_BILL_STATUS.toString()));
        list.add(new JFXCheckBox(Permission.UPDATE_PAID_BILL_STATUS.toString()));
        list.add(new JFXCheckBox(Permission.UPDATE_PREPARE_DONE_BILL_STATUS.toString()));
        list.add(new JFXCheckBox(Permission.UPDATE_SHIPPING_BILL_STATUS.toString()));
        list.add(new JFXCheckBox(Permission.UPDATE_PREPARING_BILL_STATUS.toString()));
        list.add(new JFXCheckBox(Permission.BILL_MANAGEMENT.toString()));
        list.add(new JFXCheckBox(Permission.DAILY_DISH_MANAGEMENT.toString()));
        list.add(new JFXCheckBox(Permission.DISH_MANAGEMENT.toString()));
        list.add(new JFXCheckBox(Permission.ROLE_MANAGEMENT.toString()));
        list.add(new JFXCheckBox(Permission.USER_MANAGEMENT.toString()));
        permissionField.setItems(list);
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
                if (_user != null)
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

    private ArrayList<Permission> getPermissionsSelected() {
        ArrayList<Permission> result = new ArrayList<>();

        for (JFXCheckBox permissionCheckbox : permissionField.getItems()) {
            if (permissionCheckbox.isSelected()) {
                try {
                    result.add(Permission.get(permissionCheckbox.getText()));
                } catch (IsNotAPermissionException ignored) {
                }
            }
        }

        return result;
    }
}
