package dao.restApi;

import com.google.firebase.database.annotations.NotNull;
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
    public ArrayList<UserModel> getAllUser(String token, Integer length, Integer offset) throws FetchUserFailException {
        try {
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
        } catch (IOException | RequestFailException e) {
            throw new FetchUserFailException(e.getMessage());
        }
    }

    @Override
    public UserModel createUser(String token, UserModel userModel, String password) throws CreateUserFailException {
        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        if (userModel.get_username() != null)
            body.add(new BasicNameValuePair("username", userModel.get_username()));
        if (password.length() < 6) throw new CreateUserFailException("Mật khẩu phải lớn hơn 5 ký tự");
        body.add(new BasicNameValuePair("password", password));
        if (userModel.get_email() == null) throw new CreateUserFailException("Email là bắt buộc");
        body.add(new BasicNameValuePair("email", userModel.get_email()));
        if (userModel.get_fullName() != null)
            body.add(new BasicNameValuePair("fullName", userModel.get_fullName()));
        if (userModel.get_birthday() != null)
            body.add(new BasicNameValuePair("birthday",
                    new SimpleDateFormat("yyyy-MM-dd").format(userModel.get_birthday())));
        body.add(new BasicNameValuePair("role", userModel.get_role()));
        if (userModel.get_avatar() != null) {
            try {
                String fileName = userModel.get_username() + "-" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                body.add(new BasicNameValuePair("avatar",
                        FirebaseDao.getInstance().create(fileName, userModel.get_avatar())));
            } catch (FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);
            String response = http.post("/api/users", new Header[]{header}, body);

            setPermission(token, userModel.get_username(), userModel.get_permissions());

            JSONObject json = new JSONObject(response);
            return new UserModel(json);
        } catch (IOException | RequestFailException | AddPermissionFailException e) {
            throw new CreateUserFailException(e.getMessage());
        }
    }

    @Override
    public void deleteUser(String token, String username) throws DeleteUserFailException {
        try {
            HttpConnection http = new HttpConnection();
            BasicHeader header = new BasicHeader("Authorization", token);
            http.delete("/api/users/" + username, new Header[]{header});
        } catch (IOException | RequestFailException e) {
            throw new DeleteUserFailException(e.getMessage());
        }
    }

    @Override
    public UserModel editProfile(String token, UserModel user) throws SaveUserFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        if (user.get_email() != null)
            body.add(new BasicNameValuePair("email", user.get_email()));
        if (user.get_fullName() != null)
            body.add(new BasicNameValuePair("fullName", user.get_fullName()));
        if (user.get_birthday() != null)
            body.add(new BasicNameValuePair("birthday",
                    new SimpleDateFormat("yyyy-MM-dd").format(user.get_birthday())));
        if (user.get_avatar() != null) {
            try {
                String fileName = user.get_username() + "-" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                body.add(new BasicNameValuePair("avatar",
                        FirebaseDao.getInstance().create(fileName, user.get_avatar())));
            } catch (FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String response;
        try {
            response = http.put("/api/users/" + user.get_username(), new Header[]{header}, body);
            if (user.get_permissions() != null)
                setPermission(token, user.get_username(), user.get_permissions());
        } catch (IOException | RequestFailException | AddPermissionFailException e) {
            throw new SaveUserFailException(e.getMessage());
        }

        JSONObject json = new JSONObject(response);
        return new UserModel(json);
    }

    @Override
    public UserModel setPermission(String token, String username, ArrayList<Permission> permissions) throws AddPermissionFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);

        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        for (int i = 0; i < permissions.size(); i++) {
            body.add(new BasicNameValuePair("permissions[" + i + "]", permissions.get(i).toString()));
        }

        try {
            String response = http.post("/api/users/" + username + "/permissions/set", new Header[]{header}, body);

            JSONObject json = new JSONObject(response);
            return new UserModel(json);
        } catch (IOException | RequestFailException e) {
            throw new AddPermissionFailException(e.getMessage());
        }
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
    public void changePassword(@NotNull String token, @NotNull String username, String oldPassword, @NotNull String newPassword) throws ChangePasswordFailException {
        HttpConnection http = new HttpConnection();
        BasicHeader header = new BasicHeader("Authorization", token);
        // Create body for request
        List<NameValuePair> body = new ArrayList<>();
        if (oldPassword != null)
            body.add(new BasicNameValuePair("oldPassword", oldPassword));
        body.add(new BasicNameValuePair("newPassword", newPassword));

        try {
            http.put("/api/users/" + username + "/password/", new Header[]{header}, body);
        } catch (IOException | RequestFailException e) {
            throw new ChangePasswordFailException(e.getMessage());
        }
    }
}
