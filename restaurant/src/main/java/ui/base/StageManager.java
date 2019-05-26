package ui.base;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.Stack;

public class StageManager {
    //region Singleton
    private static StageManager _ourInstance = new StageManager();
    //endregion
    private Stack<Stage> _mainStages;
    private boolean _isShowingStage;
    private EventHandler<WindowEvent> closeStageEvent = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            closeAndPopCurrent();
            if (!_isShowingStage)
                showCurrent();
        }
    };

    private StageManager() {
        _mainStages = new Stack<>();
        _isShowingStage = false;
    }

    public static StageManager getInstance() {
        return _ourInstance;
    }

    public Stage getCurrent() {
        return _mainStages.peek();
    }

    public void push(Stage stage) {
        _isShowingStage = true;
        closeCurrent();
        addNewStage(stage);
    }

    public void pushReplacement(Stage stage) {
        _isShowingStage = true;
        closeAndPopCurrent();
        addNewStage(stage);
    }

    public void push(Scene scene, String title) {
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.UTILITY);
        newStage.setTitle(title);
        newStage.setResizable(false);
        newStage.setScene(scene);
        push(newStage);
    }

    public void pushReplacement(Scene scene, String title) {
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.UTILITY);
        newStage.setTitle(title);
        newStage.setResizable(false);
        newStage.setScene(scene);
        pushReplacement(newStage);
    }

    public void pop() {
        closeAndPopCurrent();
    }

    private void addNewStage(Stage stage) {
        _mainStages.push(stage);
        stage.setOnCloseRequest(closeStageEvent);
        showCurrent();
    }

    private void closeAndPopCurrent() {
        if (_mainStages.size() > 0) {
            _mainStages.pop().close();
        }
    }

    private void closeCurrent() {
        if (_mainStages.size() > 0) {
            _mainStages.peek().close();
        }
    }

    private void showCurrent() {
        if (_mainStages.size() > 0)
            _mainStages.peek().show();
        _isShowingStage = false;
    }
}
