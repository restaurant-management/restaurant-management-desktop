package dao.exceptions.billExceptions;

public class EditBillFailException extends Exception {
    private String _message;

    public EditBillFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
