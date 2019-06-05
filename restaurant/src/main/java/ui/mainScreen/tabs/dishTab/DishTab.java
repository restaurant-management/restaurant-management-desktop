package ui.mainScreen.tabs.dishTab;

import bus.DishBus;
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
import model.DishModel;
import ui.compenents.CustomDialog;
import ui.compenents.ErrorDialog;
import ui.compenents.IconButton;
import ui.compenents.LoadingDialog;
import ui.mainScreen.tabs.dishTab.popups.AddDishPopup;
import ui.mainScreen.tabs.userTab.popups.AddUserPopup;
import ui.mainScreen.tabs.userTab.popups.EditUserPopup;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.function.Function;

@ViewController(value = "/ui/mainScreen/tabs/dishTab/DishTab.fxml")
public class DishTab {
    private final ObservableList<Dish> _dummyData = FXCollections.observableArrayList();
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTreeTableView<Dish> mainTableView;
    @FXML
    private JFXTreeTableColumn<Dish, String> nameColumn;
    @FXML
    private JFXTreeTableColumn<Dish, String> descriptionColumn;
    @FXML
    private JFXTreeTableColumn<Dish, Integer> priceColumn;
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
        ((Label) context.getRegisteredObject("TitleLabel")).setText("Danh sách món ăn");

        // Run background task load data.
        getData();
        setupTableView();
        searchField.textProperty().addListener(setupSearchField(mainTableView));
        mainTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        addButton.setOnAction(event -> {
            try {
                new CustomDialog("Thêm món ăn mới", AddDishPopup.class).show();
            } catch (FlowException e) {
                new ErrorDialog("Lỗi khi tạo của sổ mới", e.getMessage()).show();
            }
        });
        editButton.setOnAction(event -> {
            if (mainTableView.getSelectionModel().getSelectedItem() == null) {
                new ErrorDialog("Lỗi", "Vui lòng chọn món ăn cần sửa!").show();
                return;
            }
            try {
                new CustomDialog("Sửa thông tin", EditUserPopup.class,
                        mainTableView.getSelectionModel().getSelectedItem().getValue().dishId.getValue()).show();
            } catch (FlowException e) {
                e.printStackTrace();
                new ErrorDialog("Lỗi khi tạo của sổ mới", e.getMessage()).show();
            }
        });
        deleteButton.setOnAction(event -> deleteDish());
        reloadButton.setIcon(FontAwesomeIcon.UNDO, null).setOnAction(event -> getData());
    }

    private void deleteDish() {
        if (mainTableView.getSelectionModel().getSelectedItem() == null) {
            new ErrorDialog("Lỗi", "Vui lòng chọn món cần xoá!").show();
            return;
        }

        LoadingDialog _loadingDialog = new LoadingDialog("Đang xoá").show();
        Task<Void> deleteUserTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DishBus.get_instance().deleteDish(mainTableView.getSelectionModel().getSelectedItem().getValue().dishId.getValue());
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

    private void getData() {
        LoadingDialog loadingDialog = new LoadingDialog("Đang tải dữ liệu").show();
        Task<Void> getDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _dummyData.clear();
                ArrayList<DishModel> listDishModel = DishBus.get_instance().getAll();
                for (DishModel dishModel : listDishModel) {
                    _dummyData.add(new Dish(dishModel));
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
        setupCellValueFactory(nameColumn, Dish::nameProperty);
        setupCellValueFactory(descriptionColumn, Dish::descriptionProperty);
        setupCellValueFactory(priceColumn, Dish::priceProperty);
        mainTableView.setRoot(new RecursiveTreeItem<>(_dummyData, RecursiveTreeObject::getChildren));
        mainTableView.setShowRoot(false);
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<Dish> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(dishProp -> {
                    final Dish dish = dishProp.getValue();
                    return dish.name.get().contains(newVal)
                            || dish.description.get().contains(newVal)
                            || Integer.toString(dish.price.get()).contains(newVal);
                });
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Dish, T> column, Function<Dish, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Dish, T> param) -> {
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
    static final class Dish extends RecursiveTreeObject<Dish> {
        final IntegerProperty dishId;
        final StringProperty name;
        final StringProperty description;
        final IntegerProperty price;

        Dish(DishModel dishModel) {
            dishId = new SimpleIntegerProperty(dishModel.get_dishId());
            name = new SimpleStringProperty(dishModel.get_name());
            description = new SimpleStringProperty(dishModel.get_description() != null ? dishModel.get_description() : "");
            price = new SimpleIntegerProperty(dishModel.get_defaultPrice());
        }

        StringProperty nameProperty() {
            return name;
        }

        StringProperty descriptionProperty() {
            return description;
        }

        ObjectProperty<Integer> priceProperty() {
            return price.asObject();
        }
    }
}
