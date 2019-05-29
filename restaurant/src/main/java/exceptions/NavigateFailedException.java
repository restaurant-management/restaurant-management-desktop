package exceptions;

public class NavigateFailedException extends Exception {
    private String _screenName;

    public NavigateFailedException(String screenName){
        _screenName = screenName;
    }

    @Override
    public String getMessage() {
        return _screenName;
    }
}
