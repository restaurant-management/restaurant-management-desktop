package dao.exceptions.billExceptions;

public class DeleteBillFailException extends Exception {
    private String _message;

    public DeleteBillFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
