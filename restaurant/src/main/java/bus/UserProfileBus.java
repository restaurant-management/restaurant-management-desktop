package bus;

import dao.Repository;
import model.UserModel;

public class UserProfileBus {
    private Repository _repository = Repository.getInstance();

    public UserModel getCurrentUser() {
        return _repository.get_currentUser();
    }
}
