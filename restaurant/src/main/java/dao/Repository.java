package dao;

import dao.exceptions.AuthenticationFailException;
import dao.restApi.UserDao;

public class Repository {

    //region Singleton
    private static Repository _ourInstance = new Repository();

    public static Repository getInstance() {
        return _ourInstance;
    }
    //endregion

    private UserDao _userDao;

    private Repository(){
        _userDao = new UserDao();
    }

    public void login(String username, String password) throws AuthenticationFailException {
        String token = _userDao.authenticate(username, password);

        // TODO Store token
        System.out.println("Storing token...");
        // TODO Fetch and Save current user
        System.out.println("Fetch and Save current user...");
    }
}
