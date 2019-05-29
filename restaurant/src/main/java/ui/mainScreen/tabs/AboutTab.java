package ui.mainScreen.tabs;

import animatefx.animation.LightSpeedIn;
import animatefx.animation.ZoomIn;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.RandomAnimation;

import javax.annotation.PostConstruct;

@ViewController(value = "/ui/mainScreen/tabs/AboutTab.fxml")
public class AboutTab {
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
        roundedImage(avatar1);
        roundedImage(avatar2);
        roundedImage(avatar3);
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

    private void roundedImage(ImageView imageView) {
        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        // remove the rounding clip so that our effect can show through.
        imageView.setClip(null);

        // store the rounded image in the imageView.
        imageView.setImage(image);
    }
}
