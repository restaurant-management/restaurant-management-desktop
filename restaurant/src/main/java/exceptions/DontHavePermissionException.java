package exceptions;

public class DontHavePermissionException extends Exception {
    @Override
    public String getMessage() {
        return "Tài khoản không có quyền truy cập.";
    }
}
