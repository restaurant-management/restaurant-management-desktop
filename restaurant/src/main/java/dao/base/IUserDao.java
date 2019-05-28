package dao.base;

import dao.exceptions.*;
import dao.exceptions.userExceptions.AuthenticationFailException;
import dao.exceptions.userExceptions.ChangeRoleFailException;
import dao.exceptions.userExceptions.FetchUserFailException;
import dao.exceptions.userExceptions.SaveUserFailException;
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

    ArrayList<UserModel> getAllUser(String token) throws RequestFailException, IOException;

    ArrayList<UserModel> getAllUser(String token, int length, int offset) throws RequestFailException, IOException;

    UserModel editProfile(String token, UserModel user) throws SaveUserFailException;

    UserModel addPermission(String token, String username, Permission permission) throws AddPermissionFailException;

    UserModel removePermission(String token, String username, Permission permission) throws RemovePermissionFailException;

    UserModel changeRole(String token, String username, String role) throws ChangeRoleFailException;
}
