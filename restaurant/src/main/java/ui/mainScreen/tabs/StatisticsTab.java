package ui.mainScreen.tabs;

import bus.StatisticsBus;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import model.DishModel;
import org.json.JSONArray;
import org.json.JSONObject;
import ui.compenents.ErrorDialog;
import ui.compenents.LoadingDialog;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@ViewController(value = "/ui/mainScreen/tabs/StatisticsTab.fxml")
public class StatisticsTab {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private LineChart<String, Integer> billChart;
    @FXML
    private JFXButton reloadButton;
    @FXML
    private JFXDatePicker startField;
    @FXML
    private JFXDatePicker endField;

    private JSONArray _listBillData;
    private ArrayList<JSONArray> _listLegend = new ArrayList<>();
    private ArrayList<DishModel> listDish = new ArrayList<>();
    private XYChart.Series<String, Integer> series = new XYChart.Series<>();

    @PostConstruct
    void init() {
        ((Label) context.getRegisteredObject("TitleLabel")).setText("Thống kê");

        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        endField.setConverter(converter);
        startField.setConverter(converter);

        endField.setValue(LocalDate.now());
        startField.setValue(LocalDate.now().minusMonths(1));
        reloadButton.setOnAction(event -> loadBillData());
        reloadButton.fire();
        billChart.getData().add(series);
    }

    private void setupChart() {
        series.getData().clear();
        for (int i = 0; i < _listBillData.length(); i++) {
            String xValue = ((JSONObject) _listBillData.get(i)).getString("day");
            int yValue = ((JSONObject) _listBillData.get(i)).getInt("count");
            series.getData().add(new XYChart.Data<>(xValue, yValue));
        }
    }

//    private void setupChart() {
//        billChart.getData().clear();
//        int j = 0;
//        for (JSONArray data : _listLegend) {
//
//            XYChart.Series<String, Integer> series = new XYChart.Series<>();
//            series.setName(listDish.get(j).get_name());
//            for (int i = 0; i < data.length(); i++) {
//                String xValue = ((JSONObject) data.get(i)).getString("day");
//                int yValue = ((JSONObject) data.get(i)).getInt("count");
//                series.getData().add(new XYChart.Data<>(xValue, yValue));
//            }
//            billChart.getData().add(series);
//            j++;
//        }
//    }

    private void loadBillData() {
        LoadingDialog loadingDialog = new LoadingDialog("Đang tải dữ liệu").show();
        Task<Void> getDataTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _listBillData = new StatisticsBus().countBill(startField.getValue(), endField.getValue());
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                setupChart();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi tải dữ liệu", getException().getMessage()).show();
            }
        };

        new Thread(getDataTask).start();
    }
}
