package dao.restApi;

import dao.base.IUserDao;
import dao.exceptions.AuthenticationFailException;
import dao.exceptions.FetchUserFailException;
import dao.exceptions.RequestFailException;
import model.UserModel;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

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

        return token.replace("\"", "");
    }

    public UserModel getProfileByUsername(String token, String username) throws FetchUserFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        try {
            String response = http.get("/api/users/" + username, new Header[]{header});
            JSONObject json = new JSONObject(response);
            return new UserModel(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RequestFailException e) {
            throw new FetchUserFailException(e.getMessage());
        }

        return null;
    }
}
