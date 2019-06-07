package dao.exceptions;

public class RemovePermissionFailException extends Exception {
    private String _message;

    public RemovePermissionFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
