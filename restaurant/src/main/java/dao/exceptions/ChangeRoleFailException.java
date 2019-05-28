package dao.exceptions;

public class ChangeRoleFailException extends Exception {
    private String _message;

    public ChangeRoleFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
