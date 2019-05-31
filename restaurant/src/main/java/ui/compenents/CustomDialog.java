package ui.compenents;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import io.datafx.controller.flow.FlowException;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import ui.base.Popupable;
import ui.base.StageManager;

public class CustomDialog {
    private JFXDialog _alert;

    public CustomDialog(String title, Class<? extends Popupable> content, Object... parameters) throws FlowException {
        StackPane container = (StackPane) StageManager.getInstance().getCurrent().getScene().getRoot();
        JFXDialogLayout body = new JFXDialogLayout();

        body.setBody(Popupable.create(content, parameters));

        Label _title = new Label(title);
        JFXButton closeButton = new CloseButton();
        closeButton.setOnAction(action -> _alert.close());
        StackPane.setAlignment(_title, Pos.CENTER_LEFT);
        StackPane.setAlignment(closeButton, Pos.CENTER_RIGHT);
        body.setHeading(_title, closeButton);

        _alert = new JFXDialog();
        _alert.setTransitionType(JFXDialog.DialogTransition.BOTTOM);
        _alert.setDialogContainer(container);
        _alert.setOverlayClose(false);
        _alert.setContent(body);
        _alert.getContent().getStyleClass().add("custom-dialog");
    }

    public void show() {
        _alert.show();
    }

    public void close() {
        _alert.close();
    }

    public void setTransitionType(JFXDialog.DialogTransition transitionType) {
        _alert.setTransitionType(transitionType);
    }

    public void setOnDialogClosed(EventHandler<? super JFXDialogEvent> handler) {
        _alert.setOnDialogClosed(handler);
    }
}
