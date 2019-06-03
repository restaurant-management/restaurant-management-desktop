package ui.mainScreen;

import bus.AuthorizationBus;
import com.jfoenix.controls.JFXButton;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import model.enums.Permission;
import ui.mainScreen.tabs.*;
import ui.mainScreen.tabs.userTab.UserTab;

import javax.annotation.PostConstruct;
import java.util.Objects;

@ViewController(value = "/ui/mainScreen/MainDrawer.fxml")
public class MainDrawer {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXButton statisticsBtn;
    @FXML
    private JFXButton menuBtn;
    @FXML
    private JFXButton dishBtn;
    @FXML
    private JFXButton billBtn;
    @FXML
    private JFXButton roleBtn;
    @FXML
    private JFXButton userBtn;

    private AuthorizationBus _authorizationBus = new AuthorizationBus();

    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");

        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        bindNodeToController(statisticsBtn, StatisticsTab.class, contentFlow, contentFlowHandler);

        if (!_authorizationBus.checkPermission(Permission.BILL_MANAGEMENT)) {
            billBtn.setDisable(true);
        } else bindNodeToController(billBtn, BillTab.class, contentFlow, contentFlowHandler);

        if (!_authorizationBus.checkPermission(Permission.DAILY_DISH_MANAGEMENT)) {
            menuBtn.setDisable(true);
        } else bindNodeToController(menuBtn, MenuTab.class, contentFlow, contentFlowHandler);

        if (!_authorizationBus.checkPermission(Permission.DISH_MANAGEMENT)) {
            dishBtn.setDisable(true);
        } else bindNodeToController(dishBtn, DishTab.class, contentFlow, contentFlowHandler);

        if (!_authorizationBus.checkPermission(Permission.ROLE_MANAGEMENT)) {
            roleBtn.setDisable(true);
        } else bindNodeToController(roleBtn, RoleTab.class, contentFlow, contentFlowHandler);

        if (!_authorizationBus.checkPermission(Permission.USER_MANAGEMENT)) {
            userBtn.setDisable(true);
        } else bindNodeToController(userBtn, UserTab.class, contentFlow, contentFlowHandler);
    }

    private void bindNodeToController(JFXButton button, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(button.getId(), controllerClass);
        button.setOnAction(event -> {
            try {
                flowHandler.navigateTo(controllerClass);
            } catch (VetoException | FlowException e) {
                e.printStackTrace();
            }
        });
    }
}
