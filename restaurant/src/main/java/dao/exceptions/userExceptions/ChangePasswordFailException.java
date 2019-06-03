package dao.exceptions.userExceptions;

public class ChangePasswordFailException extends Exception {
    private String _message;

    public ChangePasswordFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
