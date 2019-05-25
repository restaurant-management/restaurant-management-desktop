package util;

import javafx.scene.control.Alert;

public class ErrorDialog {
    private Alert _alert;

    public ErrorDialog(String title, String message) {
        _alert = new Alert(Alert.AlertType.ERROR);
        _alert.setTitle(title);
        _alert.setContentText(message);
        _alert.setHeaderText(null);
    }

    public void show() {
        _alert.showAndWait();
    }
}
