package ui.mainScreen.tabs.userTab;

import bus.AuthenticationBus;
import bus.UserBus;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import model.UserModel;
import ui.compenents.*;
import ui.mainScreen.tabs.userTab.popups.AddUserPopup;
import ui.mainScreen.tabs.userTab.popups.EditUserPopup;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.function.Function;

@ViewController(value = "/ui/mainScreen/tabs/userTab/UserTab.fxml")
public class UserTab {
    private final ObservableList<User> _dummyData = FXCollections.observableArrayList();
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTreeTableView<User> mainTableView;
    @FXML
    private JFXTreeTableColumn<User, String> usernameColumn;
    @FXML
    private JFXTreeTableColumn<User, String> fullNameColumn;
    @FXML
    private JFXTreeTableColumn<User, String> emailColumn;
    @FXML
    private JFXTreeTableColumn<User, String> birthdayColumn;
    @FXML
    private JFXTreeTableColumn<User, String> roleColumn;
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

    public UserTab() {
    }

    @PostConstruct
    private void init() {
        ((Label) context.getRegisteredObject("TitleLabel")).setText("Danh sách người dùng");

        // Run background task load data.
        getData();
        setupTableView();
        searchField.textProperty().addListener(setupSearchField(mainTableView));
        mainTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        addButton.setOnAction(event -> {
            try {
                new CustomDialog("Thêm người dùng mới", AddUserPopup.class).show();
            } catch (FlowException e) {
                new ErrorDialog("Lỗi khi tạo của sổ mới", e.getMessage()).show();
            }
        });
        editButton.setOnAction(event -> {
            if (mainTableView.getSelectionModel().getSelectedItem() == null) {
                new ErrorDialog("Lỗi", "Vui lòng chọn người dùng cần sửa!").show();
                return;
            }
            try {
                new CustomDialog("Sửa thông tin", EditUserPopup.class,
                        mainTableView.getSelectionModel().getSelectedItem().getValue()._username.getValue()).show();
            } catch (FlowException e) {
                e.printStackTrace();
                new ErrorDialog("Lỗi khi tạo của sổ mới", e.getMessage()).show();
            }
        });
        deleteButton.setOnAction(event -> deleteUser());
        reloadButton.setIcon(FontAwesomeIcon.UNDO, null).setOnAction(event -> getData());
    }

    private void deleteUser() {
        if (mainTableView.getSelectionModel().getSelectedItem() == null) {
            new ErrorDialog("Lỗi", "Vui lòng chọn người dùng cần xoá!").show();
            return;
        } else if (new AuthenticationBus().getCurrentUser().get_username().equals(mainTableView.getSelectionModel().getSelectedItem().getValue()._username.getValue())) {
            new ErrorDialog("Lỗi", "Không thể xoá bản thân!").show();
            return;
        }

        LoadingDialog _loadingDialog = new LoadingDialog("Đang xoá").show();
        Task<Void> deleteUserTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new UserBus().deleteUser(mainTableView.getSelectionModel().getSelectedItem().getValue()._username.getValue());
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
//                new SuccessDialog("Xoá thành công", "Hãy tải lại để cập nhật").show();
                getData();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi xoá người dùng", getException().getMessage());
            }
        };
        new Thread(deleteUserTask).start();
    }

    private void setupTableView() {
        setupCellValueFactory(usernameColumn, User::usernameProperty);
        setupCellValueFactory(fullNameColumn, User::fullNameProperty);
        setupCellValueFactory(emailColumn, User::emailProperty);
        setupCellValueFactory(birthdayColumn, User::birthdayProperty);
        setupCellValueFactory(roleColumn, User::roleProperty);
        mainTableView.setRoot(new RecursiveTreeItem<>(_dummyData, RecursiveTreeObject::getChildren));
        mainTableView.setShowRoot(false);
    }

    private void getData() {
        LoadingDialog loadingDialog = new LoadingDialog("Đang tải dữ liệu").show();
        Task<Void> getDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _dummyData.clear();
                ArrayList<UserModel> listUserModel = new UserBus().getAll();
                for (UserModel userModel : listUserModel) {
                    _dummyData.add(new User(userModel));
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


    private ChangeListener<String> setupSearchField(final JFXTreeTableView<UserTab.User> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(userProp -> {
                    final User user = userProp.getValue();
                    return user._username.get().contains(newVal)
                            || user._fullName.get().contains(newVal)
                            || user._email.get().contains(newVal)
                            || user._role.get().contains(newVal)
                            || user._birthday.get().contains(newVal);
                });
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<User, T> column, Function<User, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<User, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    /*
     * data class
     */
    static final class User extends RecursiveTreeObject<User> {
        final StringProperty _username;
        final StringProperty _fullName;
        final StringProperty _avatar;
        final StringProperty _email;
        final StringProperty _birthday;
        final StringProperty _role;
        final SimpleIntegerProperty _point;

        User(UserModel userModel) {
            _username = new SimpleStringProperty(userModel.get_username());
            _fullName = new SimpleStringProperty(userModel.get_fullName() != null ? userModel.get_fullName() : "");
            _avatar = new SimpleStringProperty(userModel.get_avatar() != null ? userModel.get_avatar() : "");
            _email = new SimpleStringProperty(userModel.get_email() != null ? userModel.get_email() : "");
            _role = new SimpleStringProperty(userModel.get_role() != null ? userModel.get_role() : "");
            _birthday = new SimpleStringProperty(userModel.get_birthday() != null ? new SimpleDateFormat("dd/MM/yyyy")
                    .format(userModel.get_birthday()) : "");
            _point = new SimpleIntegerProperty(userModel.get_point() != null ? userModel.get_point() : 0);
        }

        StringProperty usernameProperty() {
            return _username;
        }

        StringProperty fullNameProperty() {
            return _fullName;
        }

        StringProperty avatarProperty() {
            return _avatar;
        }

        StringProperty emailProperty() {
            return _email;
        }

        StringProperty birthdayProperty() {
            return _birthday;
        }

        StringProperty roleProperty() {
            return _role;
        }

        IntegerProperty pointProperty() {
            return _point;
        }
    }
}
