package ui.mainScreen;

import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import ui.accountScreen.AccountScreen;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/ui/mainScreen/MainScreen.fxml")
public class MainScreen{
    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger titleBurger;
    @FXML
    private StackPane titleBurgerContainer;
    @FXML
    public JFXRippler optionsRippler;
    @FXML
    public StackPane optionsBurger;
    private JFXPopup toolbarPopup;

    @PostConstruct
    public void init() throws IOException {
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


        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPopup.fxml"));
        toolbarPopup = loader.load();

        optionsBurger.setOnMouseClicked(e ->
                toolbarPopup.show(optionsBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.RIGHT,
                        -12,
                        15));
        JFXTooltip.setVisibleDuration(Duration.millis(3000));
        JFXTooltip.install(titleBurgerContainer, burgerTooltip, Pos.BOTTOM_CENTER);

        // create the inner flow and content
        context = new ViewFlowContext();
        // set the default controller
        Flow innerFlow = new Flow(AccountScreen.class);

        final FlowHandler flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        final Duration containerAnimationDuration = Duration.millis(320);

    }

}
