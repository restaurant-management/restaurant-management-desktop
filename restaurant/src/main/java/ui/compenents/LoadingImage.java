package ui.compenents;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;

public class LoadingImage {
    private Task<Void> _task;
    private Image _placeholder;
    private Image _image;
    private String _url;

    public LoadingImage(String url, String placeholder) {
        _placeholder = new Image(placeholder);
        _url = url;
        _task = new Task<Void>() {
            @Override
            protected Void call() {
                _image = new Image(url);
                return null;
            }
        };
    }

    public LoadingImage start(ImageView imageView) {
        imageView.setImage(_placeholder);
        _task.setOnSucceeded(event -> imageView.setImage(_image));
        start();
        return this;
    }

    public LoadingImage start(Shape shape) {
        shape.setFill(new ImagePattern(_placeholder));
        _task.setOnSucceeded(event -> shape.setFill(new ImagePattern(_image)));
        start();
        return this;
    }

    private void start(){
        System.out.println("Loading image... " + _url);
        new Thread(_task).start();
    }

//    public LoadingImage setOnFaild(EventHandler<WorkerStateEvent> value) {
//        _task.setOnFailed(value);
//        return this;
//    }
}
