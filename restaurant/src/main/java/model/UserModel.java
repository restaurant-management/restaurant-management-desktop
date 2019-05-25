package model;

import org.json.JSONObject;

import java.util.Date;

public class UserModel {
    private String _uuid;
    private String _username;
    private String _fullName;
    private String _avatar;
    private String _email;
    private Date _birthday;
    private String _role;
    private int _point;

    public UserModel(JSONObject jsonObject) {
        _uuid = jsonObject.getString("uuid");
        _username = jsonObject.getString("userName");
        _fullName = jsonObject.getString("fullName");
        _avatar = jsonObject.getString("avatar");
        _email = jsonObject.getString("email");
        _birthday = (Date) jsonObject.get("birthday");
        _point = jsonObject.getInt("point");
        _role = jsonObject.getString("role");
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
    }

    public String get_uuid() {
        return _uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
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

    public int get_point() {
        return _point;
    }

    public void set_point(int _point) {
        this._point = _point;
    }
}
