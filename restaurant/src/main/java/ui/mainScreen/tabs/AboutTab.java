package ui.mainScreen.tabs;

import animatefx.animation.LightSpeedIn;
import animatefx.animation.ZoomIn;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ui.compenents.LoadingImage;
import util.RandomAnimation;

import javax.annotation.PostConstruct;

@ViewController(value = "/ui/mainScreen/tabs/AboutTab.fxml")
public class AboutTab {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private ImageView avatar1;
    @FXML
    private ImageView avatar2;
    @FXML
    private ImageView avatar3;
    @FXML
    private HBox card1;
    @FXML
    private HBox card2;
    @FXML
    private HBox card3;

    @PostConstruct
    void init() {
        ((Label) context.getRegisteredObject("TitleLabel")).setText("Nhóm phát triển");
        roundedImage(avatar1, "https://avatars0.githubusercontent.com/u/36978155?s=460");
        roundedImage(avatar2, "https://avatars1.githubusercontent.com/u/36977998?s=460");
        roundedImage(avatar3, "https://avatars2.githubusercontent.com/u/48937704?s=460");
        card1.setScaleX(0);
        card1.setScaleY(0);
        card2.setScaleX(0);
        card2.setScaleY(0);
        card3.setScaleX(0);
        card3.setScaleY(0);
        card1.setOnMouseEntered(event -> RandomAnimation.random(avatar1));
        card2.setOnMouseEntered(event -> RandomAnimation.random(avatar2));
        card3.setOnMouseEntered(event -> RandomAnimation.random(avatar3));
        avatar2.setOpacity(0);
        new ZoomIn(card1).playOnFinished(new ZoomIn(card2).playOnFinished(new ZoomIn(card3).playOnFinished(new LightSpeedIn(avatar2)))).play();
    }

    private void roundedImage(ImageView imageView, String url) {
        new LoadingImage(url, "/images/default-avatar.jpg").start(imageView);
        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);
    }
}
