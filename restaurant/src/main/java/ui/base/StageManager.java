package ui.base;

import com.jfoenix.controls.JFXDecorator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.ContainerAnimations;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Stack;

public class StageManager {
    //region Singleton
    private static StageManager _ourInstance = new StageManager();
    //endregion
    private Stack<Stage> _stageStack;
    private Stack<FlowHandler> _flowHandlerStack;

    private StageManager() {
        _stageStack = new Stack<>();
        _flowHandlerStack = new Stack<>();
    }

    public static StageManager getInstance() {
        return _ourInstance;
    }

    public Stage getCurrent() {
        return _stageStack.peek();
    }

    public void push(Class<?> startViewControllerClass, double width, double height, String title, Stage primaryStage) throws FlowException {
        // Create new stage.
        Stage newStage = prepareNewStage(primaryStage, startViewControllerClass, width, height, title);

        // Close current stage.
        if (_stageStack.size() > 0) _stageStack.peek().close();

        // Push stage to stack
        _stageStack.push(newStage);

        // Show top stage of stack
        showTopStack();
    }

    public void pushReplacement(Class<?> startViewControllerClass, double width, double height, String title, Stage primaryStage) throws FlowException {
        // Create new stage.
        Stage newStage = prepareNewStage(primaryStage, startViewControllerClass, width, height, title);

        // Close current stage and pop stack.
        if (_stageStack.size() > 0) {
            _stageStack.peek().close();
            popStack();
        }

        // Push stage to stack
        _stageStack.push(newStage);

        // Show top stage of stack
        showTopStack();
    }

    public void pop() {
        // Close current stage and pop stack.
        if (_stageStack.size() > 0) {
            _stageStack.peek().close();
            popStack();
        }

        // Show top stage of stack
        showTopStack();
    }

    public FlowHandler getCurrentFlowHandler(){
        return _flowHandlerStack.peek();
    }

    private void onStageClosed() {
        popStack();
        showTopStack();
    }

    private void popStack() {
        _stageStack.pop();
        _flowHandlerStack.pop();
    }

    /**
     * Create new stage, decorate and add on closed listener to it.
     *
     * @param startViewControllerClass Controller class of screen.
     * @param width                    Window width.
     * @param height                   Window height.
     * @param title                    Window title.
     * @return New window.
     */
    private Stage prepareNewStage(Stage stage, Class<?> startViewControllerClass, double width, double height, String title) throws FlowException {
        if (stage == null)
            stage = new Stage();

        // Create flow
        Flow flow = new Flow(startViewControllerClass);
        ExtendedAnimatedFlowContainer container = new ExtendedAnimatedFlowContainer(Duration.millis(500), ContainerAnimations.ZOOM_OUT);
        FlowHandler flowHandler = flow.createHandler();
        _flowHandlerStack.push(flowHandler);
        flowHandler.start(container);


        // Decorate stage
        JFXDecorator decorator = new JFXDecorator(stage, container.getView());
        decorator.setCustomMaximize(true);
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CUTLERY);
        icon.setFill(Color.WHITE);
        decorator.setGraphic(icon);

        stage.setTitle(title);
        Scene scene = new Scene(new StackPane(decorator), width, height);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(StageManager.class.getResource("../../css/jfoenix-main-demo.css").toExternalForm());
        stage.setScene(scene);

        //Add listener
        stage.setOnCloseRequest(event -> onStageClosed());

        return stage;
    }

    private void showTopStack() {
        if (_stageStack.size() > 0) _stageStack.peek().show();
    }
}
