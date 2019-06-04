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

@ViewController(value = "/ui/mainScreen/tabs/roleTab/popups/AddRolePopup.fxml")
public class AddRolePopup extends Popupable {
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

    @Override
    protected void get() {
    }

    @PostConstruct
    private void init() {
        submitButton.setOnAction(event -> submit());
        setupPermissionField();
    }

    private void submit() {
        if (!nameField.validate()) return;

        LoadingDialog _loadingDialog = new LoadingDialog("Đang tạo").show();
        Task<Void> submitTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                RoleModel roleModel = new RoleModel(slugField.getText(),
                        nameField.getText(),
                        descriptionField.getText(),
                        getPermissionsSelected());
                RoleBus.get_instance().createRole(roleModel);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Tạo vai trò thành công", "Vai trò đã được tạo").show();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi tạo vai trò", getException().getMessage()).show();
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
