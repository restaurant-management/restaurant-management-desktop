package ui.mainScreen.tabs.roleTab;

import bus.RoleBus;
import bus.UserBus;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeTableColumn;
import model.RoleModel;
import ui.compenents.CustomDialog;
import ui.compenents.ErrorDialog;
import ui.compenents.IconButton;
import ui.compenents.LoadingDialog;
import ui.mainScreen.tabs.roleTab.popups.AddRolePopup;
import ui.mainScreen.tabs.roleTab.popups.EditRolePopup;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.function.Function;

@ViewController(value = "/ui/mainScreen/tabs/roleTab/RoleTab.fxml")
public class RoleTab {
    private final ObservableList<Role> _dummyData = FXCollections.observableArrayList();
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTreeTableView<Role> mainTableView;
    @FXML
    private JFXTreeTableColumn<Role, String> slugColumn;
    @FXML
    private JFXTreeTableColumn<Role, String> nameColumn;
    @FXML
    private JFXTreeTableColumn<Role, String> descriptionColumn;
    @FXML
    private JFXTreeTableColumn<Role, String> permissionsColumn;
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXButton addButton;
    @FXML
    private JFXButton editButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private IconButton reloadButton;

    @PostConstruct
    private void init() {
        ((Label) context.getRegisteredObject("TitleLabel")).setText("Danh sách vai trò");
        getData();
        setupTableView();
        searchField.textProperty().addListener(setupSearchField(mainTableView));
        mainTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        addButton.setOnAction(event -> {
            try {
                new CustomDialog("Thêm vai trò mới", AddRolePopup.class).show();
            } catch (FlowException e) {
                new ErrorDialog("Lỗi khi tạo của sổ mới", e.getMessage()).show();
            }
        });
        editButton.setOnAction(event -> {
            if (mainTableView.getSelectionModel().getSelectedItem() == null) {
                new ErrorDialog("Lỗi", "Vui lòng chọn vai trò cần sửa!").show();
                return;
            }
            try {
                new CustomDialog("Sửa thông tin", EditRolePopup.class,
                        mainTableView.getSelectionModel().getSelectedItem().getValue().slug.getValue()).show();
            } catch (FlowException e) {
                e.printStackTrace();
                new ErrorDialog("Lỗi khi tạo của sổ mới", e.getMessage()).show();
            }
        });
        deleteButton.setOnAction(event -> deleteRole());
        reloadButton.setIcon(FontAwesomeIcon.UNDO, null).setOnAction(event -> getData());
    }

    private void deleteRole() {
        if (mainTableView.getSelectionModel().getSelectedItem() == null) {
            new ErrorDialog("Lỗi", "Vui lòng chọn vai trò cần xoá!").show();
            return;
        }

        LoadingDialog _loadingDialog = new LoadingDialog("Đang xoá").show();
        Task<Void> deleteUserTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                RoleBus.get_instance().deleteRole(mainTableView.getSelectionModel().getSelectedItem().getValue().slug.getValue());
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                getData();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi xoá vai trò", getException().getMessage());
            }
        };
        new Thread(deleteUserTask).start();
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Role> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(roleProp -> {
                    final Role role = roleProp.getValue();
                    return role.slug.get().contains(newVal)
                            || role.name.get().contains(newVal)
                            || role.description.get().contains(newVal)
                            || role.permissions.get().contains(newVal);
                });
    }

    private void setupTableView() {
        setupCellValueFactory(slugColumn, Role::slugProperty);
        setupCellValueFactory(nameColumn, Role::nameProperty);
        setupCellValueFactory(descriptionColumn, Role::descriptionProperty);
        setupCellValueFactory(permissionsColumn, Role::permissionsProperty);
        mainTableView.setRoot(new RecursiveTreeItem<>(_dummyData, RecursiveTreeObject::getChildren));
        mainTableView.setShowRoot(false);
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Role, T> column, Function<Role, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Role, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    private void getData() {
        LoadingDialog loadingDialog = new LoadingDialog("Đang tải dữ liệu").show();
        Task<Void> getDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _dummyData.clear();
                ArrayList<RoleModel> listRoleModel = RoleBus.get_instance().getAll();
                for (RoleModel roleModel : listRoleModel) {
                    _dummyData.add(new Role(roleModel));
                }
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.close();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi tải dữ liệu", getMessage()).show();
            }
        };

        new Thread(getDataTask).start();
    }

    /*
     * data class
     */
    static final class Role extends RecursiveTreeObject<Role> {
        final StringProperty slug;
        final StringProperty name;
        final StringProperty description;
        final StringProperty permissions;

        Role(RoleModel roleModel) {
            slug = new SimpleStringProperty(roleModel.get_slug());
            name = new SimpleStringProperty(roleModel.get_name() != null ? roleModel.get_name() : "");
            description = new SimpleStringProperty(roleModel.get_description() != null ? roleModel.get_description() : "");
            if (roleModel.get_permissions() == null) {
                permissions = new SimpleStringProperty("");
                return;
            }
            StringBuilder stringPermissions = new StringBuilder();
            for (int i = 0; i < roleModel.get_permissions().size(); i++) {
                stringPermissions.append(roleModel.get_permissions().get(i).toString());
                if (i != roleModel.get_permissions().size()) stringPermissions.append(", ");
            }
            permissions = new SimpleStringProperty(stringPermissions.toString());
        }

        StringProperty slugProperty() {
            return slug;
        }

        StringProperty nameProperty() {
            return name;
        }

        StringProperty descriptionProperty() {
            return description;
        }

        StringProperty permissionsProperty() {
            return permissions;
        }
    }
}
