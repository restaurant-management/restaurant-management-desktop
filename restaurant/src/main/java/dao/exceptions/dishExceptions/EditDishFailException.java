package dao.exceptions.dishExceptions;

public class EditDishFailException extends Exception {
    private String _message;

    public EditDishFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
