package ui.mainScreen;

import bus.AuthenticationBus;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import exceptions.NavigateFailedException;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import ui.mainScreen.tabs.AboutTab;

import javax.annotation.PostConstruct;
import java.util.Objects;

@ViewController(value = "/ui/mainScreen/MainPopup.fxml")
public class MainPopup {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXListView<?> toolbarPopupList;

    @PostConstruct
    public void init()
    {
        Objects.requireNonNull(context, "context");
    }

    @FXML
    private void logout() throws NavigateFailedException {
        new AuthenticationBus().logout();
        ((JFXPopup) context.getRegisteredObject("ToolbarPopup")).hide();
    }

    @FXML
    public void goToAbout(MouseEvent mouseEvent) throws VetoException, FlowException {
        ((FlowHandler) context.getRegisteredObject("ContentFlowHandler")).navigateTo(AboutTab.class);
        ((JFXPopup) context.getRegisteredObject("ToolbarPopup")).hide();
    }
}