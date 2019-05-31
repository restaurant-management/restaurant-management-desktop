package ui.compenents;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ui.base.StageManager;

public class ErrorDialog {
    private JFXDialog _alert;

    public ErrorDialog(String title, String message) {
        JFXDialogLayout body = new JFXDialogLayout();
        Label _title = new Label(title);
        JFXButton closeButton = new CloseButton();
        closeButton.setOnAction(action -> _alert.close());
        StackPane.setAlignment(_title, Pos.CENTER_LEFT);
        StackPane.setAlignment(closeButton, Pos.CENTER_RIGHT);
        body.setHeading(_title, closeButton);
        body.setBody(new Label(message));


        _alert = new JFXDialog();
        _alert.setTransitionType(JFXDialog.DialogTransition.CENTER);
        _alert.setDialogContainer((StackPane) StageManager.getInstance().getCurrent().getScene().getRoot());
        _alert.setOverlayClose(false);
        _alert.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
        _alert.setContent(body);
    }

    public ErrorDialog show() {
        _alert.show();
        return this;
    }

    public ErrorDialog close() {
        _alert.close();
        return this;
    }

    public ErrorDialog setOnDialogClosed(EventHandler<? super JFXDialogEvent> handler) {
        _alert.setOnDialogClosed(handler);
        return this;
    }
}
