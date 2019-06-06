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
import java.util.ArrayList;

@ViewController(value = "/ui/mainScreen/tabs/billTab/popups/AddBillPopup.fxml")
public class EditBillPopup extends Popupable {
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
    private int _billId;
    private BillModel _bill;
    private ObservableList<DailyDishModel> _dailyDishList = FXCollections.observableArrayList();
    private ObservableList<UserModel> _listUsers = FXCollections.observableArrayList();

    @Override
    protected void get() {
        _billId = (int) context.getRegisteredObject("Object0");
    }

    @PostConstruct
    private void init() {
        get();
        Platform.runLater(this::loadData);

        submitButton.setOnAction(event -> submit());
        submitButton.setText("Cập nhật");
        dayField.valueProperty().addListener((observable, oldValue, newValue) -> {
            setUpListDish();
        });
    }

    private void submit() {
        if (!timeField.validate() || !statusField.validate() || !userField.validate()) return;

        LoadingDialog _loadingDialog = new LoadingDialog("Đang cập nhật").show();
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

                _bill.set_day(LocalDateTime.of(dayField.getValue(), timeField.getValue()));
                _bill.set_status(statusField.getValue());
                _bill.set_user(userField.getValue());
                _bill.set_managerUsername(managerField.getValue().get_username());
                _bill.set_billDetails(billDetails);
                new BillBus().editBill(_bill);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Cập nhật hoá đơn thành công", "Hoá đơn đã được cập nhật").show();
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
                _bill = new BillBus().getBill(_billId);
                _dailyDishList = FXCollections.observableArrayList(new DailyDishBus().getDailyDishes(java.sql.Date.valueOf(_bill.get_day().toLocalDate())));
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
                timeField.setValue(_bill.get_day().toLocalTime());
                dayField.setValue(_bill.get_day().toLocalDate());
                dayField.setDisable(true);
                for (UserModel user : _listUsers) {
                    if (user.get_username().equals(_bill.get_managerUsername())) {
                        managerField.setValue(user);
                    }
                    if (user.get_username().equals(_bill.get_user().get_username())) {
                        userField.setValue(user);
                    }
                }
                statusField.setValue(_bill.get_status());
                for (BillDetailModel billDetailModel : _bill.get_billDetails()) {
                    HBox box = createNewDishItem(billDetailModel);
                    if (box != null)
                        _list.add(box);
                }
                _list.add(createNewDishItem(null));
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
        dishField.setItems(_list);
    }

    private HBox createNewDishItem(BillDetailModel billDetail) {
        HBox box = new HBox();
        box.setSpacing(10);
        JFXComboBox<DailyDishModel> dishComboBox = new JFXComboBox<>();
        HBox.setHgrow(dishComboBox, Priority.ALWAYS);
        dishComboBox.setPromptText("Món ăn");
        dishComboBox.setItems(_dailyDishList);
        if (billDetail != null)
            for (int i = 0; i < _dailyDishList.size(); i++) {
                if (_dailyDishList.get(i).get_dish().get_dishId() == billDetail.get_dish().get_dishId()) {
                    dishComboBox.setValue(_dailyDishList.get(i));
                    break;
                }
                if (i == _dailyDishList.size() - 1) return null;
            }
        JFXTextField countField = new JFXTextField();
        countField.setMaxWidth(60);
        countField.setPromptText("Số lượng");
        if (billDetail != null)
            countField.setText(Integer.toString(billDetail.get_quantity()));
        FontAwesomeIconView _icon = new FontAwesomeIconView(billDetail == null ? FontAwesomeIcon.PLUS : FontAwesomeIcon.MINUS);
        JFXButton iconButton = new JFXButton();
        iconButton.setGraphic(_icon);
        if (billDetail != null) {
            iconButton.setOnAction(event -> removeBillDetail(box, billDetail.get_dish().get_dishId()));
        } else {
            iconButton.setOnAction(event -> addBillDetail(countField.getText(), dishComboBox.getValue()));
        }

        box.getChildren().addAll(dishComboBox, countField, iconButton);
        return box;
    }

    private void addBillDetail(String quantity, DailyDishModel dailyDishModel) {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang thêm món ăn vào hoá đơn").show();
        Task<Void> submitTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                BillDetailModel billDetailModel = new BillDetailModel(Integer.parseInt(quantity),
                        dailyDishModel.get_price() > 0 ?
                                dailyDishModel.get_price() : dailyDishModel.get_dish().get_defaultPrice(),
                        dailyDishModel.get_dish());
                if (billDetailModel.get_dish() == null) throw new Exception("Phải chọn món ăn");
                new BillBus().addBillDetail(_billId, billDetailModel);
                _bill = new BillBus().getBill(_billId);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                _list.clear();
                for (BillDetailModel billDetailModel : _bill.get_billDetails()) {
                    HBox box = createNewDishItem(billDetailModel);
                    if (box != null)
                        _list.add(box);
                }
                _list.add(createNewDishItem(null));
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi khi thêm món ăn", getException().getMessage()).show();
            }
        };
        new Thread(submitTask).start();
    }

    private void removeBillDetail(HBox box, int dishId) {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang xoá món ăn khỏi hoá đơn").show();
        Task<Void> submitTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new BillBus().removeBillDetail(_billId, dishId);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                _list.remove(box);
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi khi thêm món ăn", getException().getMessage()).show();
            }
        };
        new Thread(submitTask).start();
    }
}
