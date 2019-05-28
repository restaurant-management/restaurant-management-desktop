package dao.exceptions.roleExceptions;

public class DeleteRoleFailException extends Exception {
    private String _message;

    public DeleteRoleFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
