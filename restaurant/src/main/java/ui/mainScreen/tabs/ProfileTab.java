package ui.mainScreen.tabs;

import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import javax.annotation.PostConstruct;


@ViewController(value = "/ui/mainScreen/tabs/ProfileTab.fxml")
public class ProfileTab {
    @FXML
    private Circle avatar;
    @FXML
    private Rectangle cover;
    @FXML
    private VBox profileBox;

    @PostConstruct
    void init() {
        avatar.setStroke(Color.SEAGREEN);
        Image im = new Image("https://avatars1.githubusercontent.com/u/36977998?s=460", false);
        avatar.setFill(new ImagePattern(im));
        avatar.setEffect(new DropShadow(+25d, 0d, +2d, Color.valueOf("#9c27b0")));

        cover.setFill(new ImagePattern(new Image("/images/cover.jpg")));
    }
}
