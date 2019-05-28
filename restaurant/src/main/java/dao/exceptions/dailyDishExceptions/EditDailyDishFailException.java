package dao.exceptions.dailyDishExceptions;

public class EditDailyDishFailException extends Exception {
    private String _message;

    public EditDailyDishFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
