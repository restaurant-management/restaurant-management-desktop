package ui.mainScreen.tabs.dishTab.popups;

import bus.DishBus;
import com.google.firebase.database.annotations.NotNull;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Platform;
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
import ui.base.Popupable;
import ui.base.StageManager;
import ui.compenents.*;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@ViewController(value = "/ui/mainScreen/tabs/dishTab/popups/AddDishPopup.fxml")
public class EditDishPopup extends Popupable {
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
    private DishModel _dish;
    private int dishId;

    @Override
    protected void get() {
        dishId = (int) context.getRegisteredObject("Object0");
    }

    @PostConstruct
    private void init() {
        get();
        Platform.runLater(() -> loadDishDetail(dishId));

        submitButton.setText("Cập nhật");
        submitButton.setOnAction(event -> submit());
    }

    private void submit() {
        if (!nameField.validate()) return;

        LoadingDialog _loadingDialog = new LoadingDialog("Đang cập nhật").show();
        Task<Void> submitTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _dish.set_name(nameField.getText());
                if (descriptionField.getText() != null)
                    _dish.set_description(descriptionField.getText());
                if (priceField.getText() != null && !priceField.getText().isEmpty())
                    _dish.set_defaultPrice(Integer.parseInt(priceField.getText()));
                _dish.set_images(imagePaths);
                DishBus.get_instance().editDish(_dish);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                new SuccessDialog("Cập nhật món ăn thành công", "Món ăn đã được cập nhật thông tin").show();
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi cập nhật món ăn", getException().getMessage()).show();
            }
        };
        new Thread(submitTask).start();
    }

    private void loadDishDetail(int dishId) {
        LoadingDialog _loadingDialog = new LoadingDialog("Đang tải").show();
        Task<Void> loadUserTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                _dish = DishBus.get_instance().getDishDetail(dishId);
                return null;
            }

            @Override
            protected void done() {
                _loadingDialog.close();
            }

            @Override
            protected void succeeded() {
                //region setup interface
                nameField.setText(_dish.get_name());
                descriptionField.setText(_dish.get_description());
                priceField.setText(Integer.toString(_dish.get_defaultPrice()));
                setUpListImage(_dish.get_images());
                //endregion
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
                new ErrorDialog("Lỗi tải thông tin", getException().getMessage()).show();
            }
        };
        new Thread(loadUserTask).start();
    }

    private void setUpListImage(@NotNull ArrayList<String> urls) {
        for (String imageUrl : urls) {
            _list.add(createNewImageItem(imageUrl));
        }
        _list.add(createNewImageItem(null));
        imageField.setItems(_list);
    }

    private StackPane createNewImageItem(String url) {
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
                        _list.add(createNewImageItem(null));
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
        if (url != null)
            imagePaths.add(url);
        new LoadingImage(url, "/images/placeholder.jpg").start(imageView);
        pane.getChildren().add(imageView);
        return pane;
    }
}
