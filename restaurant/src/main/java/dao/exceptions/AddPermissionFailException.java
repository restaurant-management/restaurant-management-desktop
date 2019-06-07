package dao.exceptions;

public class AddPermissionFailException extends Exception {
    private String _message;

    public AddPermissionFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
