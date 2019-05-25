package util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class ErrorDialog {
    private JFXDialog _alert;

    public ErrorDialog(Scene scene, String title, String message, String closeButtonText) {
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
        _alert.setDialogContainer((StackPane) scene.getRoot());
        _alert.setOverlayClose(false);
        _alert.setContent(body);
    }

    public void show() {
        _alert.show();
    }

    public void close() {
        _alert.close();
    }
}
