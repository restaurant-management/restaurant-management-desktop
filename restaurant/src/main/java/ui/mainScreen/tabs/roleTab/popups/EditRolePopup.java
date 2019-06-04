package ui.mainScreen.tabs.roleTab.popups;

import bus.RoleBus;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import model.RoleModel;
import model.enums.Permission;
import model.exceptions.IsNotAPermissionException;
import ui.base.Popupable;
import ui.compenents.ErrorDialog;
import ui.compenents.LoadingDialog;
import ui.compenents.PrimaryButton;
import ui.compenents.SuccessDialog;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Objects;

@ViewController(value = "/ui/mainScreen/tabs/roleTab/popups/AddRolePopup.fxml")
public class EditRolePopup extends Popupable {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField slugField;
    @FXML
    private JFXTextField descriptionField;
    @FXML
    private JFXListView<JFXCheckBox> permissionField;
    @FXML
    private PrimaryButton submitButton;

    private RoleModel _role;

    @Override
    protected void get() {
        String roleSlug = (String) context.getRegisteredObject("Object0");
        loadRole(roleSlug);
    }

    @PostConstruct
    private void init(){
        Objects.requireNonNull(context, "context");
        get();
        setupPermissionField();

        submitButton.setText("Cập nhật");
        submitButton.setOnAction(event -> submit());
    }

    private void submit() {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang cập nhật").show();
        Task<Void> commitChangeTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (slugField.getText() != null && !slugField.getText().isEmpty())
                    _role.set_slug(slugField.getText());
                if (nameField.getText() != null)
                    _role.set_name(nameField.getText());
                if (descriptionField.getText() != null)
                    _role.set_description(descriptionField.getText());
                _role.set_permissions(getPermissionsSelected());

                RoleBus.get_instance().editRole(_role);
                return null;
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Cập nhật vai trò thành công", "Vai trò đã được cập nhật").show();
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

    private void loadRole(String slug) {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang tải").show();
        Task<Void> loadUserTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _role = RoleBus.get_instance().getRole(slug);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                //region setup interface
                slugField.setText(_role.get_slug());
                nameField.setText(_role.get_name());
                descriptionField.setText(_role.get_description());
                //endregion
                for (JFXCheckBox permissionCheckBox : permissionField.getItems()) {
                    try {
                        if (_role.get_permissions().contains(Permission.get(permissionCheckBox.getText()))) {
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
