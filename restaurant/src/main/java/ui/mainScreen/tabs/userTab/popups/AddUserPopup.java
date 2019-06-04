package ui.mainScreen.tabs.userTab.popups;

import bus.RoleBus;
import bus.UserBus;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
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
import ui.compenents.ErrorDialog;
import ui.compenents.LoadingDialog;
import ui.compenents.PrimaryButton;
import ui.compenents.SuccessDialog;
import util.EmailValidator;

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
public class AddUserPopup extends Popupable {
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
    private String avatarPath = "";

    @Override
    protected void get() {
    }

    @PostConstruct
    private void init() {
        Objects.requireNonNull(context, "context");
        get();

        loadRole();
        setupPermissionField();


        RequiredFieldValidator validator = new RequiredFieldValidator();
        EmailValidator emailValidator = new EmailValidator();

        usernameField.getValidators().add(validator);
        passwordField.getValidators().add(validator);
        emailField.getValidators().add(emailValidator);


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
                avatarPath = file.getPath();
            } catch (IOException e) {
                new ErrorDialog("Lỗi tải hình", e.getMessage()).show();
            }
        });

        submitButton.setOnAction(event -> submit());
    }

    private void submit() {
        if (!usernameField.validate() || !passwordField.validate() || !emailField.validate()) return;

        LoadingDialog _loadingDialog = new LoadingDialog("Đang tạo").show();
        Task<Void> submitTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                UserModel userModel = new UserModel(usernameField.getText(),
                        fullNameField.getText(),
                        avatarPath,
                        emailField.getText(),
                        birthdayField.getValue() != null ?
                                Date.from(birthdayField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null,
                        roleField.getValue() != null ? roleField.getValue().get_slug() : null,
                        0, getPermissionsSelected());
                new UserBus().createUser(userModel, passwordField.getText());
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Tạo người dùng thành công", "Người dùng đã được tạo").show();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi tạo người dùng", getException().getMessage()).show();
            }
        };
        new Thread(submitTask).start();
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
