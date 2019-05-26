package dao.exceptions;

public class FetchUserFailException extends Exception {
    private String _message;

    public FetchUserFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
