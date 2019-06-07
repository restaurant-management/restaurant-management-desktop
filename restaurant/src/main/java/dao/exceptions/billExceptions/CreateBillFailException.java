package dao.exceptions.billExceptions;

public class CreateBillFailException extends Exception {
    private String _message;

    public CreateBillFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
