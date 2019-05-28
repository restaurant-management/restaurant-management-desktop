package dao.exceptions.dailyDishExceptions;

public class CreateDailyDishFailException extends Exception {
    private String _message;

    public CreateDailyDishFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
