package bus;

import dao.Repository;
import dao.exceptions.AuthenticationFailException;
import dao.exceptions.FetchUserFailException;

public class AuthenticationBus {
    private Repository _repository = Repository.getInstance();

    public boolean checkLoggedIn() {
        return _repository.checkLoggedIn();
    }

    public void login(String username, String password) throws AuthenticationFailException, FetchUserFailException {
        _repository.login(username, password);
    }

    public void logout() {
        System.out.println("Logout...");
        _repository.logout();
    }
}
