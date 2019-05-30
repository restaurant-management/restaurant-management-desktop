package util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import ui.base.StageManager;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomDialog {
    private JFXDialog _alert;

    public CustomDialog(String title, Node content, boolean showCloseButton, Node... actions) {
        JFXDialogLayout body = new JFXDialogLayout();
        body.setHeading(new Label(title));
        body.setBody(content);

        JFXButton closeButton = new JFXButton();
        closeButton.setButtonType(JFXButton.ButtonType.RAISED);
        closeButton.setText("Đóng");
        closeButton.setOnAction(action -> _alert.close());

        ArrayList<Node> listActions = new ArrayList<>();
        if (showCloseButton) listActions.add(closeButton);
        listActions.addAll(Arrays.asList(actions));
        body.setActions(listActions);

        _alert = new JFXDialog();
        _alert.setTransitionType(JFXDialog.DialogTransition.BOTTOM);
        _alert.setDialogContainer((StackPane) StageManager.getInstance().getCurrent().getScene().getRoot());
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
