package dao.exceptions.dishExceptions;

public class GetDishFailException extends Exception {
    private String _message;

    public GetDishFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
