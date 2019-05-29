package dao.exceptions.userExceptions;

public class FetchPermissionFailException extends Exception {
    private String _message;

    public FetchPermissionFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
