package dao.exceptions.dailyDishExceptions;

public class DeleteDailyDishFailException extends Exception {
    private String _message;

    public DeleteDailyDishFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
