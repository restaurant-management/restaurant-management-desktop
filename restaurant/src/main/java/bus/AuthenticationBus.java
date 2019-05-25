package bus;

import dao.Repository;
import dao.exceptions.AuthenticationFailException;

public class AuthenticationBus {
    public void login(String username, String password) throws AuthenticationFailException {
        Repository.getInstance().login(username, password);
    }
}
