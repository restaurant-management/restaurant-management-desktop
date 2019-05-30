package ui.mainScreen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.DailyDishModel;
import model.DishModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    //prepare for tableView data
    @FXML
    private TableView<DailyDishModel> tbData;
    @FXML
    private TableColumn<DailyDishModel, Date> dishDate;
    @FXML
    private TableColumn<DailyDishModel, String> dishSession;
    @FXML
    private TableColumn<DailyDishModel, Integer> dishId;
    @FXML
    private TableColumn<DailyDishModel, String> dishStatus;
    @FXML
    private TableColumn<DailyDishModel, Integer> dishPrice;
    @FXML
    private TableColumn<DailyDishModel, String> dishName;
    @FXML
    private Label dateLabel;

    @FXML
    void openDatePicker() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");

        //Set data to tableView
        dishId.setCellValueFactory(new PropertyValueFactory<>("_dishId"));
        dishSession.setCellValueFactory(new PropertyValueFactory<>("_session"));
        dishPrice.setCellValueFactory(new PropertyValueFactory<>("_price"));
//        dishDate.setCellValueFactory(new PropertyValueFactory<>("_day"));
        dishStatus.setCellValueFactory(new PropertyValueFactory<>("_status"));
        dishName.setCellValueFactory(new PropertyValueFactory<>("_dish"));
        tbData.setItems(dailyDishModel);
    }

    // add your data here from any source
    private ObservableList<DailyDishModel> dailyDishModel = FXCollections.observableArrayList(
            new DailyDishModel(new Date(), "none", 1, "in-stock", 10000, new DishModel(1, "Thịt chó", "ádsad", new ArrayList<String>(), 0))
            );


}
