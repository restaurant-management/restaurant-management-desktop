package bus;

import dao.Repository;
import dao.exceptions.userExceptions.ChangeRoleFailException;
import dao.exceptions.userExceptions.FetchPermissionFailException;
import dao.exceptions.userExceptions.FetchUserFailException;
import dao.exceptions.userExceptions.SaveUserFailException;
import model.UserModel;

public class UserProfileBus {
    private Repository _repository = Repository.getInstance();

    public UserModel getCurrentUser() {
        return _repository.get_currentUser();
    }

    public void updateCurrentUser() throws FetchPermissionFailException, FetchUserFailException {
        _repository.fetchCurrentUserProfile();
    }

    public void updateProfile(UserModel user) throws SaveUserFailException {
        _repository.updateProfile(user);
    }

    public void changeRole(UserModel userModel) throws ChangeRoleFailException {
        _repository.changeRole(userModel);
    }
}
