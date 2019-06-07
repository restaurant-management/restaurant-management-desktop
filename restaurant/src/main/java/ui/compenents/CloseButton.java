package ui.compenents;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Color;

public class CloseButton extends JFXButton {
    public CloseButton(){
        super();
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        icon.setFill(Color.WHITE);
        this.setGraphic(icon);
        initialize();
    }
    public CloseButton(String text){
        super(text);
        initialize();
        this.getStyleClass().add("close-button-text");
    }

    private void initialize(){
        this.getStyleClass().add("close-button");
    }
}
