package ui.mainScreen.tabs.dishTab.popups;

import bus.DishBus;
import bus.UserBus;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import model.DishModel;
import model.UserModel;
import ui.base.Popupable;
import ui.base.StageManager;
import ui.compenents.*;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@ViewController(value = "/ui/mainScreen/tabs/dishTab/popups/AddDishPopup.fxml")
public class AddDishPopup extends Popupable {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField descriptionField;
    @FXML
    private JFXTextField priceField;
    @FXML
    private JFXListView<StackPane> imageField;
    @FXML
    private PrimaryButton submitButton;
    private ObservableList<StackPane> _list = FXCollections.observableArrayList();
    private ArrayList<String> imagePaths = new ArrayList<>();

    @Override
    protected void get() {
    }

    @PostConstruct
    private void init() {
        setUpListImage();

        submitButton.setOnAction(event -> submit());
    }

    private void submit() {
        if (!nameField.validate()) return;

        LoadingDialog _loadingDialog = new LoadingDialog("Đang tạo").show();
        Task<Void> submitTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DishModel dishModel = new DishModel(nameField.getText(),
                        descriptionField.getText(),
                        Integer.parseInt(priceField.getText()),
                        imagePaths);
                DishBus.get_instance().createDish(dishModel);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Tạo món ăn thành công", "Món ăn đã được tạo").show();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi tạo món ăn", getException().getMessage()).show();
            }
        };
        new Thread(submitTask).start();
    }

    private void setUpListImage() {
        _list.add(createNewImageItem());
        imageField.setItems(_list);
    }

    private StackPane createNewImageItem() {
        StackPane pane = new StackPane();
        ImageView imageView = new ImageView();
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                if (_list.get(_list.size() - 1) == pane)
                    return;
                imagePaths.remove(_list.indexOf(pane));
                _list.remove(pane);
            } else {
                final FileChooser fileChooser = new FileChooser();
                //Set extension filter
                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
                File file = fileChooser.showOpenDialog(StageManager.getInstance().getCurrent());
                if (file == null) return;
                try {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    if (_list.get(_list.size() - 1) == pane) {
                        _list.add(createNewImageItem());
                        imagePaths.add(file.getPath());
                    } else {
                        imagePaths.set(_list.indexOf(pane), file.getPath());
                    }
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(image);
                } catch (IOException e) {
                    new ErrorDialog("Lỗi tải hình", e.getMessage()).show();
                }
            }
            System.out.println(imagePaths);
        });
        new LoadingImage("", "/images/placeholder.jpg").start(imageView);
        pane.getChildren().add(imageView);
        return pane;
    }
}
