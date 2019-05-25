package dao.restApi;

import dao.base.IUserDao;
import dao.exceptions.AuthenticationFailException;
import dao.exceptions.RequestFailException;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements IUserDao {
    @Override
    public String authenticate(String usernameOrEmail, String password) throws AuthenticationFailException {
        HttpConnection http = new HttpConnection();

        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("usernameOrEmail", usernameOrEmail));
        body.add(new BasicNameValuePair("password", password));

        String token = "";
        try {
            token = http.post("/api/users/login", new Header[0], body);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RequestFailException e) {
            throw new AuthenticationFailException(e.getMessage());
        }

        return token;
    }
}
