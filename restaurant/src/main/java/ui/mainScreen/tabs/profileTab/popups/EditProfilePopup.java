package ui.mainScreen.tabs.profileTab.popups;


import bus.RoleBus;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import model.RoleModel;
import model.UserModel;
import util.ErrorDialog;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@ViewController(value = "/ui/mainScreen/tabs/profileTab/popups/EditProfilePopup.fxml")
public class EditProfilePopup {
    ArrayList<RoleModel> listRoles;
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXTextField fullNameField;
    @FXML
    private JFXDatePicker birthdayField;
    @FXML
    private JFXComboBox roleField;

    public static StackPane create(UserModel user) throws FlowException {
        Flow popupFlow = new Flow(EditProfilePopup.class);
        ViewFlowContext popupContext = new ViewFlowContext();
        popupContext.register("user", user);
        FlowHandler handler = popupFlow.createHandler(popupContext);
        return handler.start();
    }

    @PostConstruct
    void init() {
        Objects.requireNonNull(context, "context");

        Task<Void> loadRoleTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                roleField.setItems(FXCollections.observableArrayList("Loading..."));
                listRoles = new RoleBus().getAll();
                return null;
            }

            @Override
            protected void succeeded() {
                roleField.setItems(FXCollections.observableArrayList(listRoles));
            }

            @Override
            protected void failed() {
                new ErrorDialog("Lỗi tải danh sách vai trò", getException().getMessage(), null).show();
            }
        };
        new Thread(loadRoleTask).start();

        UserModel user = (UserModel) context.getRegisteredObject("user");

        emailField.setText(user.get_email());
        fullNameField.setText(user.get_fullName());
        birthdayField.setValue(user.get_birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    private ArrayList<String> getRoleName(ArrayList<RoleModel> listRoles) {
        return listRoles.stream().map(RoleModel::get_name).collect(Collectors.toCollection(ArrayList::new));
    }
}
