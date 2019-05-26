package dao;

import dao.exceptions.AuthenticationFailException;
import dao.exceptions.FetchUserFailException;
import dao.restApi.UserDao;
import model.UserModel;

import java.util.prefs.Preferences;

public class Repository {

    private final static String PREF_TOKEN = "user-token";
    private final static String PREF_CURRENT_USER = "current-user-username";

    //region Singleton
    private static Repository _ourInstance;
    //endregion
    private Preferences _prefs;
    private UserDao _userDao;
    private UserModel _currentUser;

    private Repository() {
        _prefs = Preferences.userRoot();
        _userDao = new UserDao();
    }

    //region Singleton
    public static Repository getInstance() {
        return _ourInstance;
    }

    public static void initializeRepository() throws FetchUserFailException {
        if (_ourInstance == null) {
            _ourInstance = new Repository();
        }
        _ourInstance.fetchCurrentUserProfile();
    }
    //endregion

    public boolean checkLoggedIn() {
        return !_prefs.get(PREF_TOKEN, "").equals("");
    }

    public void login(String username, String password) throws AuthenticationFailException, FetchUserFailException {
        String token = _userDao.authenticate(username, password);

        System.out.println("Storing token...");
        storeToken(token, username);
        fetchCurrentUserProfile();
    }

    public void logout() {
        deleteToken();
    }

    private void fetchCurrentUserProfile() throws FetchUserFailException {
        System.out.println("Fetching user profile...");
        String username = _prefs.get(PREF_CURRENT_USER, "");
        String token = _prefs.get(PREF_TOKEN, "");
        if (!username.equals("")) {
            _currentUser = _userDao.getProfileByUsername(token, username);
        }
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
        _currentUser = null;
    }

    public UserModel get_currentUser() {
        return _currentUser;
    }
}
