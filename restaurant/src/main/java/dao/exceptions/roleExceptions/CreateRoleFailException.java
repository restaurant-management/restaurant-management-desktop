package dao.exceptions.roleExceptions;

public class CreateRoleFailException extends Exception {
    private String _message;

    public CreateRoleFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
