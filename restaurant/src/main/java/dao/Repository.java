package dao;

import com.google.firebase.database.annotations.NotNull;
import dao.exceptions.AddPermissionFailException;
import dao.exceptions.dishExceptions.CreateDishFailException;
import dao.exceptions.dishExceptions.DeleteDishFailException;
import dao.exceptions.dishExceptions.EditDishFailException;
import dao.exceptions.dishExceptions.GetDishFailException;
import dao.exceptions.roleExceptions.CreateRoleFailException;
import dao.exceptions.roleExceptions.DeleteRoleFailException;
import dao.exceptions.roleExceptions.GetRoleFailException;
import dao.exceptions.userExceptions.*;
import dao.restApi.*;
import model.DishModel;
import model.RoleModel;
import model.UserModel;
import model.enums.Permission;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Repository {

    private final static String PREF_TOKEN = "user-token";
    private final static String PREF_CURRENT_USER = "current-user-username";

    //region Singleton
    private static Repository _ourInstance;
    //endregion
    private Preferences _prefs;
    private UserDao _userDao;
    private RoleDao _roleDao;
    private BillDao _billDao;
    private DishDao _dishDao;
    private DailyDishDao _dailyDishDao;
    private UserModel _currentUser;
    private ArrayList<Permission> _currentUserPermissions;

    private Repository() {
        _prefs = Preferences.userRoot();
        _userDao = new UserDao();
        _roleDao = new RoleDao();
        _billDao = new BillDao();
        _dishDao = new DishDao();
        _dailyDishDao = new DailyDishDao();
    }

    //region Singleton
    public static Repository getInstance() {
        return _ourInstance;
    }

    public static void initializeRepository() throws FetchUserFailException, FetchPermissionFailException {
        if (_ourInstance == null) {
            _ourInstance = new Repository();
        }
        _ourInstance.fetchCurrentUserProfile();
    }
    //endregion

    public boolean checkLoggedIn() {
        return !_prefs.get(PREF_TOKEN, "").equals("");
    }

    public void login(String username, String password) throws AuthenticationFailException, FetchUserFailException, FetchPermissionFailException {
        String token = _userDao.authenticate(username, password);

        System.out.println("Storing token...");
        storeToken(token, username);
        fetchCurrentUserProfile();
    }

    public void logout() {
        deleteToken();
    }

    public void fetchCurrentUserProfile() throws FetchUserFailException, FetchPermissionFailException {
        System.out.println("Fetching user profile...");
        String username = _prefs.get(PREF_CURRENT_USER, "");
        String token = getToken();
        if (!username.equals("")) {
            _currentUser = _userDao.getProfileByUsername(token, username);
            System.out.println("Fetching user permissions...");
            _currentUserPermissions = _userDao.getAllPermissions(token, username);
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
        _currentUserPermissions = null;
    }

    public UserModel get_currentUser() {
        return _currentUser;
    }

    public ArrayList<Permission> get_currentUserPermissions() {
        return _currentUserPermissions;
    }

    public void updateProfile(UserModel user) throws SaveUserFailException {
        _userDao.editProfile(getToken(), user);
    }

    public ArrayList<RoleModel> getAllRoles() throws GetRoleFailException {
        return _roleDao.getAllRole(getToken(), null, null);
    }

    public RoleModel getRole(String slug) throws GetRoleFailException {
        return _roleDao.getRole(getToken(), slug);
    }

    public void addRole(RoleModel roleModel) throws CreateRoleFailException {
        _roleDao.createRole(getToken(), roleModel.get_name(), roleModel.get_slug(), roleModel.get_description(), roleModel.get_permissions());
    }

    public void editRole(RoleModel roleModel) throws EditDishFailException, AddPermissionFailException {
        RoleModel newRole = _roleDao.editRole(getToken(), roleModel.get_baseSlug(), roleModel.get_slug(), roleModel.get_name(), roleModel.get_description());
        _roleDao.setPermission(getToken(), newRole.get_slug(), roleModel.get_permissions());
    }

    public void deleteRole(@NotNull String slug) throws DeleteRoleFailException {
        _roleDao.deleteRole(getToken(), slug);
    }

    public void changeRole(@NotNull UserModel userModel) throws ChangeRoleFailException {
        _userDao.changeRole(getToken(), userModel.get_username(), userModel.get_role());
    }

    public void changePassword(@NotNull UserModel userModel, String oldPassword, @NotNull String newPassword) throws ChangePasswordFailException {
        _userDao.changePassword(getToken(), userModel.get_username(), oldPassword, newPassword);
    }

    public ArrayList<UserModel> getAllUser() throws FetchUserFailException {
        return _userDao.getAllUser(getToken(), null, null);
    }

    public UserModel createUser(@NotNull UserModel userModel, @NotNull String password) throws CreateUserFailException {
        return _userDao.createUser(getToken(), userModel, password);
    }

    public UserModel getUserProfile(@NotNull String username) throws FetchUserFailException {
        return _userDao.getProfileByUsername(getToken(), username);
    }

    public void deleteUser(@NotNull String username) throws DeleteUserFailException {
        _userDao.deleteUser(getToken(), username);
    }

    public ArrayList<DishModel> getAllDish() throws GetDishFailException {
        return _dishDao.getAll(getToken(), null, null);
    }

    public void createDish(DishModel dishModel) throws CreateDishFailException {
        _dishDao.createDish(getToken(), dishModel.get_name(), dishModel.get_description(), dishModel.get_images(), dishModel.get_defaultPrice());
    }

    public void deleteDish(int dishId) throws DeleteDishFailException {
        _dishDao.deleteDish(getToken(), dishId);
    }
}
