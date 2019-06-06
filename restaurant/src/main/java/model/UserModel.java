package model;

import model.enums.Permission;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserModel {
    private String _uuid;
    private String _username;
    private String _fullName;
    private String _avatar;
    private String _email;
    private Date _birthday;
    private String _role;
    private Integer _point;
    private ArrayList<Permission> _permissions;

    public UserModel(JSONObject jsonObject) {
        _uuid = jsonObject.getString("uuid");
        _username = jsonObject.getString("userName");
        try {
            _fullName = jsonObject.getString("fullName");
        } catch (JSONException e) {
            _fullName = null;
        }
        try {
            _avatar = jsonObject.getString("avatar");
        } catch (JSONException e) {
            _avatar = null;
        }
        _email = jsonObject.getString("email");
        try {
            _birthday = new SimpleDateFormat("yyyy-MM-dd")
                    .parse(jsonObject.getString("birthday"));
        } catch (Exception e) {
            _birthday = null;
        }
        _point = jsonObject.getInt("point");
        _role = jsonObject.getString("role");
        try {
            JSONArray jsonPermissions = jsonObject.getJSONArray("permissions");
            _permissions = new ArrayList<>();
            for (Object object : jsonPermissions) {
                try {
                    _permissions.add(Permission.get((String) object));
                } catch (Exception ignore) {
                }
            }
        } catch (Exception e) {
            _permissions = null;
        }
    }

    public UserModel(UserModel user) {
        _uuid = user._uuid;
        _username = user._username;
        _fullName = user._fullName;
        _avatar = user._avatar;
        _email = user._email;
        _birthday = user._birthday;
        _point = user._point;
        _role = user._role;
        _permissions = user._permissions;
    }

    public UserModel(String username, String fullName, String avatar, String email, Date birthday, String role, Integer point, ArrayList<Permission> permissions) {
        _username = username;
        _fullName = fullName;
        _avatar = avatar;
        _email = email;
        _birthday = birthday;
        _role = role;
        _point = point;
        _permissions = permissions;
    }

    public ArrayList<Permission> get_permissions() {
        return _permissions;
    }

    public void set_permissions(ArrayList<Permission> _permissions) {
        this._permissions = _permissions;
    }

    public String get_uuid() {
        return _uuid;
    }

    public String get_username() {
        return _username;
    }

    public String get_fullName() {
        return _fullName;
    }

    public void set_fullName(String _fullName) {
        this._fullName = _fullName;
    }

    public String get_avatar() {
        return _avatar;
    }

    public void set_avatar(String _avatar) {
        this._avatar = _avatar;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public Date get_birthday() {
        return _birthday;
    }

    public void set_birthday(Date _birthday) {
        this._birthday = _birthday;
    }

    public String get_role() {
        return _role;
    }

    public void set_role(String _role) {
        this._role = _role;
    }

    public Integer get_point() {
        return _point;
    }

    public void set_point(Integer _point) {
        this._point = _point;
    }

    @Override
    public String toString() {
        return _username;
    }
}
