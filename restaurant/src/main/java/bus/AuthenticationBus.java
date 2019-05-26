package bus;

import dao.Repository;
import dao.exceptions.AuthenticationFailException;
import dao.exceptions.FetchUserFailException;
import javafx.scene.Scene;
import ui.base.StageManager;
import ui.signInScreen.SignInScreen;

import java.io.IOException;

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
        try {
            StageManager.getInstance().pushReplacement(new Scene(new SignInScreen()), "Đăng nhập");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
