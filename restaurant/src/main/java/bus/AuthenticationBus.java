package bus;

import dao.Repository;
import dao.exceptions.userExceptions.AuthenticationFailException;
import dao.exceptions.userExceptions.FetchPermissionFailException;
import dao.exceptions.userExceptions.FetchUserFailException;
import exceptions.DontHavePermissionException;
import exceptions.NavigateFailedException;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import model.UserModel;
import ui.base.StageManager;
import ui.signInScreen.SignInScreen;

public class AuthenticationBus {
    private Repository _repository = Repository.getInstance();
    private AuthorizationBus _authorization = new AuthorizationBus();

    public boolean checkLoggedIn() {
        return _repository.checkLoggedIn();
    }

    public void login(String username, String password) throws AuthenticationFailException, FetchUserFailException, FetchPermissionFailException, DontHavePermissionException {
        _repository.login(username, password);
        _authorization.authorization();
    }

    public void logout() throws NavigateFailedException {
        System.out.println("Logout...");
        _repository.logout();
        try {
            StageManager.getInstance().getCurrentFlowHandler().navigateTo(SignInScreen.class);
        } catch (VetoException | FlowException e) {
            e.printStackTrace();
            throw new NavigateFailedException("Sign in screen");
        }
    }

    public UserModel getCurrentUser() {
        return _repository.get_currentUser();
    }
}
