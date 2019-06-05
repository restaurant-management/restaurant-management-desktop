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
public class AddDailyDishPopup extends Popupable {
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

    @Override
    protected void get() {
        _date = (Date) context.getRegisteredObject("Object0");
    }

    @PostConstruct
    private void init() {
        get();
        setupSessionField();
        setupStatusField();
        Platform.runLater(this::setupDishField);

        submitButton.setOnAction(event -> submit());
    }

    private void submit() {
        if (!dishNameField.validate() || !sessionField.validate() || !statusField.validate()) return;

        LoadingDialog _loadingDialog = new LoadingDialog("Đang thêm").show();
        Task<Void> submitTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DailyDishModel dailyDishModel = new DailyDishModel(_date,
                        sessionField.getValue(),
                        statusField.getValue(),
                        priceField.getText() != null && !priceField.getText().isEmpty() ?
                                Integer.parseInt(priceField.getText()) : 0,
                        dishNameField.getValue());
                new DailyDishBus().createDailyDish(dailyDishModel);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Thêm món ăn hằng ngày thành công", "Món ăn đã được thêm vào menu").show();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi khi thêm món ăn", getException().getMessage()).show();
            }
        };
        new Thread(submitTask).start();
    }

    private void setupDishField() {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang tải danh sách món ăn").show();
        Task<Void> loadDishTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _listDish = DishBus.get_instance().get_listDishes();
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                dishNameField.setItems(FXCollections.observableArrayList(_listDish));
            }

            @Override
            protected void failed() {
                new ErrorDialog("Lỗi tải danh sách món ăn", getException().getMessage()).show();
            }
        };
        new Thread(loadDishTask).start();
    }

    private void setupSessionField() {
        ObservableList<DaySession> list = FXCollections.observableArrayList();
        list.add(DaySession.NONE);
        list.add(DaySession.MORNING);
        list.add(DaySession.NOON);
        list.add(DaySession.AFTERNOON);
        list.add(DaySession.EVENING);
        sessionField.setItems(list);
        sessionField.setValue(DaySession.NONE);
        sessionField.setConverter(new StringConverter<DaySession>() {
            @Override
            public String toString(DaySession object) {
                return object.toDisplayString();
            }

            @Override
            public DaySession fromString(String string) {
                try {
                    return DaySession.get(string);
                } catch (IsNotADaySessionException e) {
                    e.printStackTrace();
                    return DaySession.NONE;
                }
            }
        });
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
