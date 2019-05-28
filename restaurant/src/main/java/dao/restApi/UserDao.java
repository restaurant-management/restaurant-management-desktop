package dao.restApi;

import dao.base.IUserDao;
import dao.exceptions.*;
import dao.exceptions.userExceptions.AuthenticationFailException;
import dao.exceptions.userExceptions.ChangeRoleFailException;
import dao.exceptions.userExceptions.FetchUserFailException;
import dao.exceptions.userExceptions.SaveUserFailException;
import model.UserModel;
import model.enums.Permission;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

        String token;
        try {
            token = http.post("/api/users/login", new Header[0], body);
        } catch (IOException | RequestFailException e) {
            throw new AuthenticationFailException(e.getMessage());
        }

        return token.replace("\"", "");
    }

    @Override
    public UserModel getProfileByUsername(String token, String username) throws FetchUserFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        try {
            String response = http.get("/api/users/" + username, new Header[]{header});
            JSONObject json = new JSONObject(response);
            return new UserModel(json);
        } catch (RequestFailException | IOException e) {
            throw new FetchUserFailException(e.getMessage());
        }
    }

    @Override
    public UserModel getProfileByEmail(String token, String email) throws FetchUserFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        try {
            String response = http.get("/api/users/email" + email, new Header[]{header});
            JSONObject json = new JSONObject(response);
            return new UserModel(json);
        } catch (RequestFailException | IOException e) {
            throw new FetchUserFailException(e.getMessage());
        }
    }

    @Override
    public ArrayList<UserModel> getAllUser(String token) throws RequestFailException, IOException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        String response = http.get("/api/users", new Header[]{header});

        ArrayList<UserModel> result = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for (Object json : jsonArray) {
            result.add(new UserModel((JSONObject) json));
        }
        return result;
    }

    @Override
    public ArrayList<UserModel> getAllUser(String token, int length, int offset) throws RequestFailException, IOException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        String uri = "/api/users?length=" + length + "&offset=" + offset;
        String response = http.get(uri, new Header[]{header});

        ArrayList<UserModel> result = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for (Object json : jsonArray) {
            result.add(new UserModel((JSONObject) json));
        }
        return result;
    }

    @Override
    public UserModel editProfile(String token, UserModel user) throws SaveUserFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("email", user.get_email()));
        body.add(new BasicNameValuePair("fullName", user.get_fullName()));
        body.add(new BasicNameValuePair("birthday", new SimpleDateFormat("yyyy-MM-dd").format(user.get_birthday())));
        body.add(new BasicNameValuePair("avatar", user.get_avatar()));

        String response;
        try {
            response = http.put("/api/users/" + user.get_username(), new Header[]{header}, body);
        } catch (IOException | RequestFailException e) {
            throw new SaveUserFailException(e.getMessage());
        }

        JSONObject json = new JSONObject(response);
        return new UserModel(json);
    }

    @Override
    public UserModel addPermission(String token, String username, Permission permission) throws AddPermissionFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        String response;
        try {
            response = http.post("/api/users/" + username + "/permissions/" + permission, new Header[]{header}, new ArrayList<>());
        } catch (IOException | RequestFailException e) {
            throw new AddPermissionFailException(e.getMessage());
        }

        JSONObject json = new JSONObject(response);
        return new UserModel(json);
    }

    @Override
    public UserModel removePermission(String token, String username, Permission permission) throws RemovePermissionFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        String response;
        try {
            response = http.delete("/api/users/" + username + "/permissions/" + permission, new Header[]{header});
        } catch (IOException | RequestFailException e) {
            throw new RemovePermissionFailException(e.getMessage());
        }

        JSONObject json = new JSONObject(response);
        return new UserModel(json);
    }

    @Override
    public UserModel changeRole(String token, String username, String role) throws ChangeRoleFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        String response;
        try {
            response = http.put("/api/users/" + username + "/role/" + role, new Header[]{header}, new ArrayList<>());
        } catch (IOException | RequestFailException e) {
            throw new ChangeRoleFailException(e.getMessage());
        }

        JSONObject json = new JSONObject(response);
        return new UserModel(json);
    }
}
