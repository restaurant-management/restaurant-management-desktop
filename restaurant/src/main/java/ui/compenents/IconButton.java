package ui.compenents;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class IconButton extends JFXButton {
    public IconButton(){
        initialize();
    }

    public IconButton(FontAwesomeIcon icon, Paint iconColor) {
        super();
        setIcon(icon, iconColor);
        initialize();
    }

    public IconButton setIcon(FontAwesomeIcon icon, Paint iconColor){
        FontAwesomeIconView _icon = new FontAwesomeIconView(icon);
        _icon.setFill(Color.WHITE);
        if (iconColor != null) _icon.setFill(iconColor);
        this.setGraphic(_icon);
        return this;
    }

    private void initialize() {
        this.getStyleClass().add("icon-button");
    }
}
