package ui.base;

import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.scene.layout.StackPane;

public abstract class Popupable {

    public static StackPane create(Class<? extends Popupable> content, Object... objects) throws FlowException {
        Flow popupFlow = new Flow(content);
        ViewFlowContext popupContext = new ViewFlowContext();
        for (int i = 0; i < objects.length; i++) {
            popupContext.register("Object" + i, objects[i]);
        }
        FlowHandler handler = popupFlow.createHandler(popupContext);
        return handler.start();
    }

    protected abstract void get();
}
