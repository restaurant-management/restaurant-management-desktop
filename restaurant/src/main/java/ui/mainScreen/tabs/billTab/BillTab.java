package ui.mainScreen.tabs.billTab;

import bus.BillBus;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import model.BillDetailModel;
import model.BillModel;
import ui.compenents.CustomDialog;
import ui.compenents.ErrorDialog;
import ui.compenents.IconButton;
import ui.compenents.LoadingDialog;
import ui.mainScreen.tabs.billTab.popups.AddBillPopup;
import ui.mainScreen.tabs.billTab.popups.EditBillPopup;
import ui.mainScreen.tabs.dishTab.popups.EditDishPopup;
import util.DateUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

@ViewController(value = "/ui/mainScreen/tabs/billTab/BillTab.fxml")
public class BillTab {
    private final ObservableList<Bill> _dummyData = FXCollections.observableArrayList();
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTreeTableView<Bill> mainTableView;
    @FXML
    private JFXTreeTableColumn<Bill, Integer> idColumn;
    @FXML
    private JFXTreeTableColumn<Bill, String> dayColumn;
    @FXML
    private JFXTreeTableColumn<Bill, String> statusColumn;
    @FXML
    private JFXTreeTableColumn<Bill, String> userColumn;
    @FXML
    private JFXTreeTableColumn<Bill, String> managerColumn;
    @FXML
    private JFXTreeTableColumn<Bill, Integer> countColumn;
    @FXML
    private JFXTreeTableColumn<Bill, Integer> moneyColumn;
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
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private IconButton backButton;
    @FXML
    private IconButton nextButton;
    private Date _date = new Date();

    @PostConstruct
    private void init() {
        ((Label) context.getRegisteredObject("TitleLabel")).setText("Danh sách hoá đơn");

        setupDatePicker();
        setupTableView();
        searchField.textProperty().addListener(setupSearchField(mainTableView));
        mainTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        addButton.setOnAction(event -> {
            try {
                new CustomDialog("Thêm hoá đơn mới", AddBillPopup.class, _date).setOnDialogClosed(event1 -> getData()).show();
            } catch (FlowException e) {
                new ErrorDialog("Lỗi khi tạo của sổ mới", e.getMessage()).show();
            }
        });
        editButton.setOnAction(event -> {
            if (mainTableView.getSelectionModel().getSelectedItem() == null) {
                new ErrorDialog("Lỗi", "Vui lòng chọn hoá đơn cần sửa!").show();
                return;
            }
            try {
                new CustomDialog("Sửa thông tin", EditBillPopup.class,
                        mainTableView.getSelectionModel().getSelectedItem().getValue().billId.getValue())
                        .setOnDialogClosed(event1 -> getData()).show();
            } catch (FlowException e) {
                e.printStackTrace();
                new ErrorDialog("Lỗi khi tạo của sổ mới", e.getMessage()).show();
            }
        });
        deleteButton.setOnAction(event -> deleteBill());
        reloadButton.setIcon(FontAwesomeIcon.UNDO, null).setOnAction(event -> getData());
    }

    private void deleteBill() {
        if (mainTableView.getSelectionModel().getSelectedItem() == null) {
            new ErrorDialog("Lỗi", "Vui lòng chọn hoá đơn cần xoá!").show();
            return;
        }

        LoadingDialog _loadingDialog = new LoadingDialog("Đang xoá").show();
        Task<Void> deleteUserTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new BillBus().deleteBill(mainTableView.getSelectionModel().getSelectedItem().getValue().billId.getValue());
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
                new ErrorDialog("Lỗi xoá hoá đơn", getException().getMessage());
            }
        };
        new Thread(deleteUserTask).start();
    }

    private void getData() {
        LoadingDialog loadingDialog = new LoadingDialog("Đang tải dữ liệu").show();
        Task<Void> getDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _dummyData.clear();
                ArrayList<BillModel> listBillModel = new BillBus().getByDay(_date);
                for (BillModel billModel : listBillModel) {
                    _dummyData.add(new Bill(billModel));
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

    private void setupDatePicker() {
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) {
                _date = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
                getData();
            }
        });
        datePicker.setValue(_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });
        backButton.setIcon(FontAwesomeIcon.ARROW_LEFT, Color.BLACK).setOnAction(event ->
                datePicker.setValue(DateUtil.decrease(_date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
        nextButton.setIcon(FontAwesomeIcon.ARROW_RIGHT, Color.BLACK).setOnAction(event ->
                datePicker.setValue(DateUtil.increase(_date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
    }

    private void setupTableView() {
        setupCellValueFactory(idColumn, Bill::billIdProperty);
        setupCellValueFactory(dayColumn, Bill::dayProperty);
        setupCellValueFactory(statusColumn, Bill::statusProperty);
        setupCellValueFactory(userColumn, Bill::userProperty);
        setupCellValueFactory(managerColumn, Bill::managerProperty);
        setupCellValueFactory(countColumn, Bill::countProperty);
        setupCellValueFactory(moneyColumn, Bill::moneyProperty);
        mainTableView.setRoot(new RecursiveTreeItem<>(_dummyData, RecursiveTreeObject::getChildren));
        mainTableView.setShowRoot(false);
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Bill> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(billProp -> {
                    final Bill bill = billProp.getValue();
                    return Integer.toString(bill.billId.get()).contains(newVal)
                            || bill.day.get().contains(newVal)
                            || bill.status.get().contains(newVal)
                            || bill.user.get().contains(newVal)
                            || bill.manager.get().contains(newVal)
                            || Integer.toString(bill.count.get()).contains(newVal)
                            || Integer.toString(bill.money.get()).contains(newVal);
                });
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Bill, T> column, Function<Bill, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Bill, T> param) -> {
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
    static final class Bill extends RecursiveTreeObject<Bill> {
        final IntegerProperty billId;
        final StringProperty day;
        final StringProperty user;
        final StringProperty manager;
        final StringProperty status;
        final IntegerProperty count;
        final IntegerProperty money;

        Bill(BillModel billModel) {
            billId = new SimpleIntegerProperty(billModel.get_billId());
            day = new SimpleStringProperty(billModel.get_day().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            user = new SimpleStringProperty(billModel.get_user().get_username());
            manager = new SimpleStringProperty(billModel.get_managerUsername() != null ? billModel.get_managerUsername() : "");
            count = new SimpleIntegerProperty(billModel.get_billDetails().size());
            status = new SimpleStringProperty(billModel.get_status().toDisplayString());
            int sum = 0;
            for (BillDetailModel billDetail : billModel.get_billDetails()) {
                sum += billDetail.get_price() * billDetail.get_quantity();
            }
            money = new SimpleIntegerProperty(sum);
        }

        ObjectProperty<Integer> moneyProperty() {
            return money.asObject();
        }

        StringProperty statusProperty() {
            return status;
        }

        ObjectProperty<Integer> billIdProperty() {
            return billId.asObject();
        }

        StringProperty dayProperty() {
            return day;
        }

        StringProperty userProperty() {
            return user;
        }

        StringProperty managerProperty() {
            return manager;
        }

        ObjectProperty<Integer> countProperty() {
            return count.asObject();
        }
    }
}
