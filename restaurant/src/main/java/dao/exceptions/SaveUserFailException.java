package dao.exceptions;

public class SaveUserFailException extends Exception {
    private String _message;

    public SaveUserFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
