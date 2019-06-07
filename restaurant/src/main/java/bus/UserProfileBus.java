package bus;

import dao.Repository;
import dao.exceptions.userExceptions.*;
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

    public void changePassword(UserModel userModel, String oldPassword, String newPassword) throws ChangePasswordFailException {
        _repository.changePassword(userModel, oldPassword, newPassword);
    }

    public UserModel getUserProfile(String username) throws FetchUserFailException {
        return _repository.getUserProfile(username);
    }
}
