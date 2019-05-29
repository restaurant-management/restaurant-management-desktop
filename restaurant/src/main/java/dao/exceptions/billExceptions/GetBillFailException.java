package dao.exceptions.billExceptions;

public class GetBillFailException extends Exception {
    private String _message;

    public GetBillFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
