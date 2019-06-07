package dao.exceptions.dailyDishExceptions;

public class GetDailyDishFailException extends Exception {
    private String _message;

    public GetDailyDishFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
