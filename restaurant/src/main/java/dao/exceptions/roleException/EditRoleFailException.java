package dao.exceptions.roleException;

public class EditRoleFailException extends Exception {
    private String _message;

    public EditRoleFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
