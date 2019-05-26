package dao;

import dao.exceptions.AuthenticationFailException;
import dao.restApi.UserDao;

import java.util.prefs.Preferences;

public class Repository {

    private final static String PREF_TOKEN = "user-token";
    private final static String PREF_CURRENT_USER = "current-user-username";

    //region Singleton
    private static Repository _ourInstance = new Repository();
    //endregion
    private Preferences _prefs;
    private UserDao _userDao;

    private Repository() {
        _prefs = Preferences.userRoot();
        _userDao = new UserDao();
    }

    //region Singleton
    public static Repository getInstance() {
        return _ourInstance;
    }
    //endregion

    public boolean checkLoggedIn() {
        return !_prefs.get(PREF_TOKEN, "").equals("");
    }

    public void login(String username, String password) throws AuthenticationFailException {
        String token = _userDao.authenticate(username, password);


        System.out.println("Storing token...");
        storeToken(token, username);
        // TODO Fetch and Save current user
        System.out.println(getToken());
    }

    public void logout() {
        deleteToken();
    }

    private void storeToken(String token, String username) {
        _prefs.put(PREF_TOKEN, token);
        _prefs.put(PREF_CURRENT_USER, username);
    }

    private String getToken() {
        return _prefs.get(PREF_TOKEN, "");
    }

    private void deleteToken() {
        _prefs.remove(PREF_TOKEN);
        _prefs.remove(PREF_CURRENT_USER);
    }
}
