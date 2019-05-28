package bus;

import dao.Repository;
import dao.exceptions.userExceptions.FetchUserFailException;

public class AppStartedBus {
    public void initializeLogic() throws FetchUserFailException {
        Repository.initializeRepository();
    }
}
