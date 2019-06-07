package dao.base;

import dao.exceptions.AddPermissionFailException;
import dao.exceptions.RemovePermissionFailException;
import dao.exceptions.userExceptions.*;
import model.UserModel;
import model.enums.Permission;

import java.io.IOException;
import java.util.ArrayList;

public interface IUserDao {
    /**
     * Authentication.
     *
     * @param usernameOrEmail Username or email.
     * @param password        Password.
     * @return If authenticate success return token.
     * @throws AuthenticationFailException If authenticate fail throw exception.
     */
    String authenticate(String usernameOrEmail, String password) throws AuthenticationFailException;

    /**
     * Get user profile by username.
     *
     * @param token    Authorization token.
     * @param username Username of user to get profile.
     * @return Whether success return User profile
     * @throws FetchUserFailException Whether fail throw exception.
     */
    UserModel getProfileByUsername(String token, String username) throws FetchUserFailException, IOException;

    /**
     * Get user profile by email.
     *
     * @param token Authorization token.
     * @param email Email of user to get profile.
     * @return Whether success return user profile.
     * @throws FetchUserFailException Whether fail throw exception.
     */
    UserModel getProfileByEmail(String token, String email) throws FetchUserFailException;

    ArrayList<UserModel> getAllUser(String token, Integer length, Integer offset) throws FetchUserFailException;

    UserModel createUser(String token, UserModel userModel, String password) throws CreateUserFailException;

    void deleteUser(String token, String username) throws DeleteUserFailException;

    UserModel editProfile(String token, UserModel user) throws SaveUserFailException;

    UserModel setPermission(String token, String username, ArrayList<Permission> permissions) throws AddPermissionFailException;

    UserModel addPermission(String token, String username, Permission permission) throws AddPermissionFailException;

    UserModel removePermission(String token, String username, Permission permission) throws RemovePermissionFailException;

    ArrayList<Permission> getAllPermissions(String token, String username) throws FetchPermissionFailException;

    UserModel changeRole(String token, String username, String role) throws ChangeRoleFailException;

    void changePassword(String token, String username, String oldPassword, String newPassword) throws ChangePasswordFailException;
}
