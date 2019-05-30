package bus;

import dao.Repository;
import dao.exceptions.userExceptions.SaveUserFailException;
import model.UserModel;

public class UserProfileBus {
    private Repository _repository = Repository.getInstance();

    public UserModel getCurrentUser() {
        return _repository.get_currentUser();
    }

    public void updateProfile(UserModel user) throws SaveUserFailException {
        _repository.updateProfile(user);
    }
}
