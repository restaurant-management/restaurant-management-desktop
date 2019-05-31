package ui.compenents;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Color;

public class PrimaryButton extends JFXButton {
    public PrimaryButton(){
        super();
        initialize();
    }

    public PrimaryButton(FontAwesomeIcon icon){
        super();
        FontAwesomeIconView _icon = new FontAwesomeIconView(icon);
        _icon.setFill(Color.WHITE);
        this.setGraphic(_icon);
        initialize();
    }

    public PrimaryButton(String text){
        super(text);
        initialize();
    }

    private void initialize(){
        this.getStyleClass().add("primary-button");
    }
}
