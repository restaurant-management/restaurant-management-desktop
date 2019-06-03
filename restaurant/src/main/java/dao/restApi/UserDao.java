package dao.restApi;

import dao.FirebaseDao;
import dao.base.IUserDao;
import dao.exceptions.AddPermissionFailException;
import dao.exceptions.RemovePermissionFailException;
import dao.exceptions.RequestFailException;
import dao.exceptions.userExceptions.*;
import model.UserModel;
import model.enums.Permission;
import model.exceptions.IsNotAPermissionException;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public ArrayList<UserModel> getAllUser(String token, Integer length, Integer offset) throws RequestFailException, IOException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        String uri = "/api/users?";
        if (length != null) uri += "length=" + length;
        if (offset != null) uri += "&offset=" + offset;
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
        if (user.get_email() != null && user.get_email().isChanged())
            body.add(new BasicNameValuePair("email", user.get_email().get_value()));
        if (user.get_fullName() != null && user.get_fullName().isChanged())
            body.add(new BasicNameValuePair("fullName", user.get_fullName().get_value()));
        if (user.get_birthday() != null && user.get_birthday().isChanged())
            body.add(new BasicNameValuePair("birthday",
                    new SimpleDateFormat("yyyy-MM-dd").format(user.get_birthday().get_value())));
        if (user.get_avatar() != null && user.get_avatar().isChanged()) {
            try {
                String fileName = user.get_username() + "-" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                body.add(new BasicNameValuePair("avatar",
                        FirebaseDao.getInstance().create(fileName, user.get_avatar().get_value())));
            } catch (FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
    public ArrayList<Permission> getAllPermissions(String token, String username) throws FetchPermissionFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);

            String response = http.get("/api/users/" + username + "/permissions/", new Header[]{header});

            JSONArray jsonArray = new JSONArray(response);
            ArrayList<Permission> result = new ArrayList<>();
            for (Object object : jsonArray) {
                try {
                    result.add(Permission.get((String) object));
                } catch (IsNotAPermissionException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } catch (IOException | RequestFailException e) {
            throw new FetchPermissionFailException(e.getMessage());
        }
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

    @Override
    public void changePassword(String token, String username, String oldPassword, String newPassword) throws ChangePasswordFailException {
        if (oldPassword == null || newPassword == null)
            throw new ChangePasswordFailException("Thiếu mật khẩu cũ hoặc mật khẩu mới");

        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("oldPassword", oldPassword));
        body.add(new BasicNameValuePair("newPassword", newPassword));

        try {
            http.put("/api/users/" + username + "/password/", new Header[]{header}, body);
        } catch (IOException | RequestFailException e) {
            throw new ChangePasswordFailException(e.getMessage());
        }
    }
}
