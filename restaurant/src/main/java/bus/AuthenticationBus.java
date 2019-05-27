package bus;

import dao.Repository;
import dao.exceptions.AuthenticationFailException;
import dao.exceptions.FetchUserFailException;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import ui.base.StageManager;
import ui.signInScreen.SignInScreen;

public class AuthenticationBus {
    private Repository _repository = Repository.getInstance();

    public boolean checkLoggedIn() {
        return _repository.checkLoggedIn();
    }

    public void login(String username, String password) throws AuthenticationFailException, FetchUserFailException {
        _repository.login(username, password);
    }

    public void logout() throws VetoException, FlowException {
        System.out.println("Logout...");
        _repository.logout();
        StageManager.getInstance().getCurrentFlowHandler().navigateTo(SignInScreen.class);
    }
}
