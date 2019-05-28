package dao.exceptions.dishExceptions;

public class DeleteDishFailException extends Exception {
    private String _message;

    public DeleteDishFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
