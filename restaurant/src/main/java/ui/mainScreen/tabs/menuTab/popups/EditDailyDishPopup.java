package ui.mainScreen.tabs.menuTab.popups;

import bus.DailyDishBus;
import bus.DishBus;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.util.StringConverter;
import model.DailyDishModel;
import model.DishModel;
import model.enums.DailyDishStatus;
import model.enums.DaySession;
import model.exceptions.IsNotADailyDishStatusException;
import model.exceptions.IsNotADaySessionException;
import ui.base.Popupable;
import ui.compenents.ErrorDialog;
import ui.compenents.LoadingDialog;
import ui.compenents.PrimaryButton;
import ui.compenents.SuccessDialog;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;

@ViewController(value = "/ui/mainScreen/tabs/menuTab/popups/AddDailyDishPopup.fxml")
public class EditDailyDishPopup extends Popupable {
    private ArrayList<DishModel> _listDish = new ArrayList<>();
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXComboBox<DishModel> dishNameField;
    @FXML
    private JFXComboBox<DaySession> sessionField;
    @FXML
    private JFXComboBox<DailyDishStatus> statusField;
    @FXML
    private JFXTextField priceField;
    @FXML
    private PrimaryButton submitButton;
    private Date _date;
    private DaySession _session;
    private Integer _dishID;
    private DailyDishModel _dailyDish;

    @Override
    protected void get() {
        _date = (Date) context.getRegisteredObject("Object0");
        _session = (DaySession) context.getRegisteredObject("Object1");
        _dishID = (Integer) context.getRegisteredObject("Object2");
    }

    @PostConstruct
    private void init() {
        get();
        setupStatusField();
        Platform.runLater(this::loadData);

        submitButton.setText("Cập nhật");
        submitButton.setOnAction(event -> submit());
    }

    private void loadData() {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang tải").show();
        Task<Void> loadUserTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _dailyDish = new DailyDishBus().getDailyDish(_date, _session, _dishID);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                //region setup interface
                dishNameField.setPromptText(_dailyDish.get_dish().toString());
                dishNameField.setDisable(true);
                sessionField.setPromptText(_dailyDish.get_session().toDisplayString());
                sessionField.setDisable(true);
                statusField.setValue(_dailyDish.get_status());
                priceField.setText(Integer.toString(_dailyDish.get_price()));

            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi tải thông tin", getException().getMessage()).show();
            }
        };
        new Thread(loadUserTask).start();
    }

    private void submit() {
        if (!statusField.validate()) return;

        LoadingDialog _loadingDialog = new LoadingDialog("Đang cập nhật").show();
        Task<Void> submitTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _dailyDish.set_price(Integer.parseInt(priceField.getText()));
                _dailyDish.set_status(statusField.getValue());
                new DailyDishBus().editDailyDish(_dailyDish);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Cập nhật món ăn hằng ngày thành công", "Món ăn đã được cập nhật").show();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi khi cập nhật món ăn", getException().getMessage()).show();
            }
        };
        new Thread(submitTask).start();
    }

    private void setupStatusField() {
        ObservableList<DailyDishStatus> list = FXCollections.observableArrayList();
        list.add(DailyDishStatus.IN_STOCK);
        list.add(DailyDishStatus.OUT_OF_STOCK);
        statusField.setItems(list);
        statusField.setValue(DailyDishStatus.IN_STOCK);
        statusField.setConverter(new StringConverter<DailyDishStatus>() {
            @Override
            public String toString(DailyDishStatus object) {
                return object.toDisplayString();
            }

            @Override
            public DailyDishStatus fromString(String string) {
                try {
                    return DailyDishStatus.get(string);
                } catch (IsNotADailyDishStatusException e) {
                    e.printStackTrace();
                    return DailyDishStatus.IN_STOCK;
                }
            }
        });
    }
}
