package dao.exceptions.userExceptions;

public class DeleteUserFailException extends Exception {
    private String _message;

    public DeleteUserFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
