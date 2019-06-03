package ui.mainScreen.tabs.userTab;

import bus.UserBus;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
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
import javafx.scene.control.TreeTableColumn;
import model.UserModel;
import ui.compenents.ErrorDialog;
import ui.compenents.LoadingDialog;

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

    public UserTab() {
    }

    @PostConstruct
    private void init() {
        ((Label) context.getRegisteredObject("TitleLabel")).setText("Danh sách người dùng");

        // Run background task load data.
        getData();
        setupTableView();
        searchField.textProperty().addListener(setupSearchField(mainTableView));
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
            _fullName = new SimpleStringProperty(userModel.get_fullName().get_value());
            _avatar = new SimpleStringProperty(userModel.get_avatar().get_value());
            _email = new SimpleStringProperty(userModel.get_email().get_value());
            _role = new SimpleStringProperty(userModel.get_role().get_value());
            _birthday = new SimpleStringProperty(new SimpleDateFormat("dd/MM/yyyy")
                    .format(userModel.get_birthday().get_value()));
            _point = new SimpleIntegerProperty(userModel.get_point().get_value());
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
