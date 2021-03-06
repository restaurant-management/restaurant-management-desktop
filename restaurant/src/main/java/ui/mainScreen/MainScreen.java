package ui.mainScreen;

import bus.AuthenticationBus;
import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import model.UserModel;
import ui.base.ExtendedAnimatedFlowContainer;
import ui.mainScreen.tabs.AboutTab;
import ui.mainScreen.tabs.StatisticsTab;
import ui.mainScreen.tabs.profileTab.ProfileTab;

import javax.annotation.PostConstruct;

import static io.datafx.controller.flow.container.ContainerAnimations.SWIPE_RIGHT;

@ViewController(value = "/ui/mainScreen/MainScreen.fxml")
public class MainScreen {
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private Label titleLabel;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXDrawer profileDrawer;
    @FXML
    private JFXHamburger titleBurger;
    @FXML
    private StackPane titleBurgerContainer;
    @FXML
    private JFXRippler optionsRippler;
    @FXML
    private StackPane optionsBurger;
    private JFXPopup toolbarPopup;

    @PostConstruct
    public void init() throws FlowException {
        UserModel user = new AuthenticationBus().getCurrentUser();
        titleLabel.setText(user.get_fullName() != null ? user.get_fullName() : user.get_username());
        final JFXTooltip burgerTooltip = new JFXTooltip("Mở menu");

        drawer.setOnDrawerOpening(e -> {
            final Transition animation = titleBurger.getAnimation();
            burgerTooltip.setText("Đóng menu");
            animation.setRate(1);
            animation.play();
        });
        drawer.setOnDrawerClosing(e -> {
            final Transition animation = titleBurger.getAnimation();
            burgerTooltip.setText("Mở menu");
            animation.setRate(-1);
            animation.play();
        });
        titleBurgerContainer.setOnMouseClicked(e -> {
            if (drawer.isClosed() || drawer.isClosing()) {
                drawer.open();
            } else {
                drawer.close();
            }
        });
        drawer.open();

        // set flow for content
        context = new ViewFlowContext();
        Flow innerFlow = new Flow(StatisticsTab.class);
        final FlowHandler flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        context.register("TitleLabel", titleLabel);
        final Duration containerAnimationDuration = Duration.millis(320);
        drawer.setContent(flowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration, SWIPE_RIGHT)));
        profileDrawer.setContent(drawer);

        // set flow for popup
        Flow popupFlow = new Flow(MainPopup.class);
        final FlowHandler popupFlowHandler = popupFlow.createHandler(context);
        toolbarPopup = new JFXPopup(popupFlowHandler.start());
        context.register("ToolbarPopup", toolbarPopup);
        optionsBurger.setOnMouseClicked(e ->
                toolbarPopup.show(optionsBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.RIGHT,
                        0,
                        50));
        JFXTooltip.setVisibleDuration(Duration.millis(3000));
        JFXTooltip.install(titleBurgerContainer, burgerTooltip, Pos.BOTTOM_CENTER);

        // set flow for drawer
        Flow sideMenuFlow = new Flow(MainDrawer.class);
        Flow profileFlow = new Flow(ProfileTab.class);
        drawer.setSidePane(sideMenuFlow.createHandler(context).start());
        profileDrawer.setSidePane(profileFlow.createHandler(context).start());

        //Add tab not in drawer to flow
        innerFlow.withGlobalLink("aboutTab", AboutTab.class);
    }

    @FXML
    private void goToProfile() {
        if (profileDrawer.isClosed() || profileDrawer.isClosing()) {
            profileDrawer.open();
        } else {
            profileDrawer.close();
        }
    }

}
