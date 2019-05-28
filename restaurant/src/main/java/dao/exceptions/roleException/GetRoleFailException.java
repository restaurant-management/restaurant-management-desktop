package dao.exceptions.roleException;

public class GetRoleFailException extends Exception {
    private String _message;

    public GetRoleFailException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
