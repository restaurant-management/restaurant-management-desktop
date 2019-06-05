package ui.mainScreen.tabs.menuTab;

import bus.DailyDishBus;
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
import model.DailyDishModel;
import model.enums.DaySession;
import ui.compenents.CustomDialog;
import ui.compenents.ErrorDialog;
import ui.compenents.IconButton;
import ui.compenents.LoadingDialog;
import ui.mainScreen.tabs.menuTab.popups.AddDailyDishPopup;
import ui.mainScreen.tabs.menuTab.popups.EditDailyDishPopup;
import util.DateUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

@ViewController(value = "/ui/mainScreen/tabs/menuTab/MenuTab.fxml")
public class MenuTab {
    private final ObservableList<DailyDish> _dummyData = FXCollections.observableArrayList();
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTreeTableView<DailyDish> mainTableView;
    @FXML
    private JFXTreeTableColumn<DailyDish, String> dishNameColumn;
    @FXML
    private JFXTreeTableColumn<DailyDish, String> sessionColumn;
    @FXML
    private JFXTreeTableColumn<DailyDish, Integer> priceColumn;
    @FXML
    private JFXTreeTableColumn<DailyDish, Integer> defaultPriceColumn;
    @FXML
    private JFXTreeTableColumn<DailyDish, String> statusColumn;
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
        ((Label) context.getRegisteredObject("TitleLabel")).setText("Menu món ăn hằng ngày");

        addButton.setOnAction(event -> {
            try {
                new CustomDialog("Thêm món mới", AddDailyDishPopup.class, _date).setOnDialogClosed(event1 -> getData()).show();
            } catch (FlowException e) {
                new ErrorDialog("Lỗi khi tạo của sổ mới", e.getMessage()).show();
            }
        });
        editButton.setOnAction(event -> {
            if (mainTableView.getSelectionModel().getSelectedItem() == null) {
                new ErrorDialog("Lỗi", "Vui lòng chọn món cần sửa!").show();
                return;
            }
            try {
                new CustomDialog("Sửa thông tin", EditDailyDishPopup.class, _date,
                        mainTableView.getSelectionModel().getSelectedItem().getValue().sessionValue,
                        mainTableView.getSelectionModel().getSelectedItem().getValue().dishId.getValue())
                        .setOnDialogClosed(event1 -> getData()).show();
            } catch (FlowException e) {
                e.printStackTrace();
                new ErrorDialog("Lỗi khi tạo của sổ mới", e.getMessage()).show();
            }
        });
        deleteButton.setOnAction(event -> deleteDailyDish());
        reloadButton.setIcon(FontAwesomeIcon.UNDO, null).setOnAction(event -> getData());

        setupDatePicker();
        setupTableView();
        searchField.textProperty().addListener(setupSearchField(mainTableView));
        mainTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void deleteDailyDish() {
        if (mainTableView.getSelectionModel().getSelectedItem() == null) {
            new ErrorDialog("Lỗi", "Vui lòng chọn món cần xoá!").show();
            return;
        }

        LoadingDialog _loadingDialog = new LoadingDialog("Đang xoá").show();
        Task<Void> deleteUserTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new DailyDishBus().deleteDailyDish(_date,
                        mainTableView.getSelectionModel().getSelectedItem().getValue().sessionValue,
                        mainTableView.getSelectionModel().getSelectedItem().getValue().dishId.getValue());
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
                new ErrorDialog("Lỗi xoá món ăn", getException().getMessage());
            }
        };
        new Thread(deleteUserTask).start();
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

    private void getData() {
        LoadingDialog loadingDialog = new LoadingDialog("Đang tải dữ liệu").show();
        Task<Void> getDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _dummyData.clear();
                ArrayList<DailyDishModel> listDailyDishModel = new DailyDishBus().getDailyDishes(_date);
                for (DailyDishModel dailyDishModel : listDailyDishModel) {
                    _dummyData.add(new DailyDish(dailyDishModel));
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

    private void setupTableView() {
        setupCellValueFactory(dishNameColumn, DailyDish::dishNameProperty);
        setupCellValueFactory(sessionColumn, DailyDish::sessionProperty);
        setupCellValueFactory(priceColumn, DailyDish::priceProperty);
        setupCellValueFactory(defaultPriceColumn, DailyDish::defaultPriceProperty);
        setupCellValueFactory(statusColumn, DailyDish::statusProperty);
        mainTableView.setRoot(new RecursiveTreeItem<>(_dummyData, RecursiveTreeObject::getChildren));
        mainTableView.setShowRoot(false);
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<DailyDish, T> column, Function<DailyDish, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<DailyDish, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<DailyDish> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(dailyDishProp -> {
                    final DailyDish dailyDish = dailyDishProp.getValue();
                    return dailyDish.dishName.get().contains(newVal)
                            || dailyDish.session.get().contains(newVal)
                            || Integer.toString(dailyDish.price.get()).contains(newVal)
                            || Integer.toString(dailyDish.defaultPrice.get()).contains(newVal)
                            || dailyDish.status.get().contains(newVal);
                });
    }

    /*
     * data class
     */
    static final class DailyDish extends RecursiveTreeObject<DailyDish> {

        final StringProperty dishName;
        final IntegerProperty dishId;
        final StringProperty session;
        final DaySession sessionValue;
        final IntegerProperty price;
        final IntegerProperty defaultPrice;
        final StringProperty status;

        DailyDish(DailyDishModel dailyDishModel) {
            dishId = new SimpleIntegerProperty(dailyDishModel.get_dish().get_dishId());
            dishName = new SimpleStringProperty(dailyDishModel.get_dish().get_name());
            session = new SimpleStringProperty(dailyDishModel.get_session().toDisplayString());
            sessionValue = dailyDishModel.get_session();
            price = new SimpleIntegerProperty(dailyDishModel.get_price());
            defaultPrice = new SimpleIntegerProperty(dailyDishModel.get_dish().get_defaultPrice());
            status = new SimpleStringProperty(dailyDishModel.get_status().toDisplayString());
        }

        StringProperty dishNameProperty() {
            return dishName;
        }

        IntegerProperty dishIdProperty() {
            return dishId;
        }

        StringProperty sessionProperty() {
            return session;
        }

        ObjectProperty<Integer> priceProperty() {
            return price.asObject();
        }

        ObjectProperty<Integer> defaultPriceProperty() {
            return defaultPrice.asObject();
        }

        StringProperty statusProperty() {
            return status;
        }
    }
}
