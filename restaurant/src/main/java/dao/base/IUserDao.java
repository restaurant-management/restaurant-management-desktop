package dao.base;

import dao.exceptions.AuthenticationFailException;

public interface IUserDao {
    /**
     * @param usernameOrEmail Username or email.
     * @param password Password.
     * @return If authenticate success return token.
     * @throws AuthenticationFailException If authenticate fail throw exception.
     */
    String authenticate(String usernameOrEmail, String password) throws AuthenticationFailException;
}
