package dao.exceptions.userExceptions;

public class AuthenticationFailException extends Exception {
    private String _message;

    public AuthenticationFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
