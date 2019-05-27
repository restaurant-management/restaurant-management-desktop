package ui.mainScreen;

import bus.AuthenticationBus;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.context.ActionHandler;
import io.datafx.controller.flow.context.FlowActionHandler;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;

public class MainPopup {
    @FXML
    public JFXPopup toolbarPopup;

    @FXML
    private JFXListView<?> toolbarPopupList;

    @ActionHandler
    private FlowActionHandler actionHandler;

    // close application
    @FXML
    private void logout() throws VetoException, FlowException {
        new AuthenticationBus().logout();
    }
}