package dao.exceptions.dishExceptions;

public class CreateDishFailException extends Exception {
    private String _message;

    public CreateDishFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
