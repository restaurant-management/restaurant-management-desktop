package bus;

import dao.Repository;
import dao.exceptions.userExceptions.FetchPermissionFailException;
import dao.exceptions.userExceptions.FetchUserFailException;
import exceptions.DontHavePermissionException;

public class AppStartedBus {
    public void initializeLogic() throws FetchUserFailException, FetchPermissionFailException, DontHavePermissionException {
        Repository.initializeRepository();
        new AuthorizationBus().authorization();
    }
}
