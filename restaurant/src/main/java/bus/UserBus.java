package bus;

import dao.Repository;
import dao.exceptions.userExceptions.FetchUserFailException;
import model.UserModel;

import java.util.ArrayList;

public class UserBus {
    private Repository _repository = Repository.getInstance();

    public ArrayList<UserModel> getAll() throws FetchUserFailException {
        return _repository.getAllUser();
    }
}
