package dao.exceptions.userExceptions;

public class CreateUserFailException extends Exception {
    private String _message;

    public CreateUserFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
