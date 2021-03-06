package ui.compenents;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import ui.base.StageManager;

public class LoadingDialog {
    private JFXDialog _dialog;

    public LoadingDialog(String title) {
        JFXDialogLayout body = new JFXDialogLayout();
        body.setHeading(new Label(title != null ? title : "Đang tải..."));
        body.setBody(new JFXSpinner());
        JFXButton closeButton = new JFXButton();
        closeButton.setOnAction(action -> _dialog.close());
        body.setActions(closeButton);

        _dialog = new JFXDialog();
        _dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        _dialog.setDialogContainer((StackPane) StageManager.getInstance().getCurrent().getScene().getRoot());
        _dialog.setOverlayClose(false);
        _dialog.setContent(body);
    }

    public LoadingDialog show() {
        _dialog.show();
        return this;
    }

    public LoadingDialog close() {
        _dialog.close();
        return this;
    }
}
