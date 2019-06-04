package bus;

import com.google.firebase.database.annotations.NotNull;
import dao.Repository;
import dao.exceptions.userExceptions.CreateUserFailException;
import dao.exceptions.userExceptions.DeleteUserFailException;
import dao.exceptions.userExceptions.FetchUserFailException;
import model.UserModel;
import model.enums.Permission;

import java.util.ArrayList;

public class UserBus {
    private Repository _repository = Repository.getInstance();

    public ArrayList<UserModel> getAll() throws FetchUserFailException {
        return _repository.getAllUser();
    }

    public void createUser(@NotNull UserModel userModel, @NotNull String password) throws CreateUserFailException {
        _repository.createUser(userModel, password);
    }

    public void deleteUser(@NotNull String username) throws DeleteUserFailException {
        _repository.deleteUser(username);
    }


}
