package util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ui.base.StageManager;

public class ErrorDialog {
    private JFXDialog _alert;

    public ErrorDialog(String title, String message, String closeButtonText) {
        JFXDialogLayout body = new JFXDialogLayout();
        body.setHeading(new Label(title));
        body.setBody(new Label(message));
        JFXButton closeButton = new JFXButton();
        closeButton.setButtonType(JFXButton.ButtonType.RAISED);
        closeButton.setText(closeButtonText != null ? closeButtonText : "Đóng");
        closeButton.setOnAction(action -> _alert.close());
        body.setActions(closeButton);


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
