package ui.mainScreen.tabs.billTab.popups;

import bus.BillBus;
import bus.DailyDishBus;
import bus.UserBus;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import model.BillDetailModel;
import model.BillModel;
import model.DailyDishModel;
import model.UserModel;
import model.enums.BillStatus;
import model.exceptions.IsNotABillStatusException;
import ui.base.Popupable;
import ui.compenents.ErrorDialog;
import ui.compenents.LoadingDialog;
import ui.compenents.PrimaryButton;
import ui.compenents.SuccessDialog;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@ViewController(value = "/ui/mainScreen/tabs/billTab/popups/AddBillPopup.fxml")
public class AddBillPopup extends Popupable {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXDatePicker dayField;
    @FXML
    private JFXTimePicker timeField;
    @FXML
    private JFXComboBox<BillStatus> statusField;
    @FXML
    private JFXComboBox<UserModel> userField;
    @FXML
    private JFXComboBox<UserModel> managerField;
    @FXML
    private JFXListView<HBox> dishField;
    @FXML
    private PrimaryButton submitButton;

    private ObservableList<HBox> _list = FXCollections.observableArrayList();
    private Date _date;
    private ObservableList<DailyDishModel> _dailyDish = FXCollections.observableArrayList();
    private ObservableList<UserModel> _listUsers = FXCollections.observableArrayList();

    @Override
    protected void get() {
        _date = (Date) context.getRegisteredObject("Object0");
    }

    @PostConstruct
    private void init() {
        get();
        Platform.runLater(this::loadData);

        submitButton.setOnAction(event -> submit());
        dayField.setValue(_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dayField.valueProperty().addListener((observable, oldValue, newValue) -> {
            _date = java.sql.Date.valueOf(newValue);
            loadData();
        });
    }

    private void submit() {
        if (!timeField.validate() || !statusField.validate() || !userField.validate()) return;

        LoadingDialog _loadingDialog = new LoadingDialog("Đang tạo").show();
        Task<Void> submitTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ArrayList<BillDetailModel> billDetails = new ArrayList<>();
                for (HBox box : dishField.getItems()) {
                    if (((JFXComboBox) box.getChildren().get(0)).getValue() != null) {
                        int quantity = Integer.parseInt(((JFXTextField) box.getChildren().get(1)).getText());
                        DailyDishModel dailyDishModel = (DailyDishModel) ((JFXComboBox) box.getChildren().get(0)).getValue();
                        billDetails.add(new BillDetailModel(quantity,
                                dailyDishModel.get_price() > 0 ?
                                        dailyDishModel.get_price() : dailyDishModel.get_dish().get_defaultPrice(),
                                dailyDishModel.get_dish()));
                    }
                }

                if (billDetails.size() == 0) throw new Exception("Mỗi hoá đơn phải có ít nhất một món ăn");

                BillModel billModel = new BillModel(LocalDateTime.of(dayField.getValue(), timeField.getValue()),
                        statusField.getValue(), userField.getValue(),
                        managerField.getValue().get_username(), billDetails);
                new BillBus().createBill(billModel);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Tạo hoá đơn thành công", "Hoá đơn đã được tạo").show();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi tạo hoá đơn", getException().getMessage()).show();
            }
        };
        new Thread(submitTask).start();
    }

    private void loadData() {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang tải").show();
        Task<Void> loadUserTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _dailyDish = FXCollections.observableArrayList(new DailyDishBus().getDailyDishes(_date));
                _listUsers = FXCollections.observableArrayList(new UserBus().getAll());
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                //region setup interface
                setUpListDish();
                setupStatusField();
                userField.setItems(_listUsers);
                managerField.setItems(_listUsers);
                timeField.setValue(LocalTime.now());
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi tải thông tin", getException().getMessage()).show();
            }
        };
        new Thread(loadUserTask).start();
    }

    private void setupStatusField() {
        ObservableList<BillStatus> list = FXCollections.observableArrayList();
        list.add(BillStatus.CREATED);
        list.add(BillStatus.PAID);
        list.add(BillStatus.PREPARING);
        list.add(BillStatus.PREPARE_DONE);
        list.add(BillStatus.DELIVERING);
        list.add(BillStatus.SHIPPING);
        list.add(BillStatus.COMPLETE);
        statusField.setItems(list);
        statusField.setConverter(new StringConverter<BillStatus>() {
            @Override
            public String toString(BillStatus object) {
                return object.toDisplayString();
            }

            @Override
            public BillStatus fromString(String string) {
                try {
                    return BillStatus.get(string);
                } catch (IsNotABillStatusException e) {
                    e.printStackTrace();
                    return BillStatus.CREATED;
                }
            }
        });
    }

    private void setUpListDish() {
        _list.clear();
        _list.add(createNewDishItem());
        dishField.setItems(_list);
    }

    private HBox createNewDishItem() {
        HBox box = new HBox();
        box.setSpacing(10);
        JFXComboBox<DailyDishModel> dishComboBox = new JFXComboBox<>();
        HBox.setHgrow(dishComboBox, Priority.ALWAYS);
        dishComboBox.setPromptText("Món ăn");
        dishComboBox.setItems(_dailyDish);
        JFXTextField countField = new JFXTextField();
        countField.setMaxWidth(60);
        countField.setPromptText("Số lượng");
        FontAwesomeIconView _icon = new FontAwesomeIconView(FontAwesomeIcon.MINUS);
        JFXButton iconButton = new JFXButton();
        iconButton.setGraphic(_icon);
        iconButton.setOnAction(event -> {
            if (_list.get(_list.size() - 1) == box)
                return;
            _list.remove(box);
        });

        // Add new box when choose dish
        dishComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue && newValue != null) {
                _list.add(createNewDishItem());
            }
        });

        box.getChildren().addAll(dishComboBox, countField, iconButton);
        return box;
    }
}
