package model;

import model.base.ChangeableProperty;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserModel {
    private String _uuid;
    private String _username;
    private ChangeableProperty<String> _fullName;
    private ChangeableProperty<String> _avatar;
    private ChangeableProperty<String> _email;
    private ChangeableProperty<Date> _birthday;
    private ChangeableProperty<String> _role;
    private ChangeableProperty<Integer> _point;
    public UserModel(JSONObject jsonObject) {
        _uuid = jsonObject.getString("uuid");
        _username = jsonObject.getString("userName");
        try {
            _fullName = new ChangeableProperty<>(jsonObject.getString("fullName"));
        } catch (JSONException e) {
            _fullName = null;
        }
        try {
            _avatar = new ChangeableProperty<>(jsonObject.getString("avatar"));
        } catch (JSONException e) {
            _avatar = null;
        }
        _email = new ChangeableProperty<>(jsonObject.getString("email"));
        try {
            _birthday = new ChangeableProperty<>(new SimpleDateFormat("yyyy-MM-dd")
                    .parse(jsonObject.getString("birthday")));
        } catch (Exception e) {
            _birthday = null;
        }
        _point = new ChangeableProperty<>(jsonObject.getInt("point"));
        _role = new ChangeableProperty<>(jsonObject.getString("role"));
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

    public String get_username() {
        return _username;
    }

    public ChangeableProperty<String> get_fullName() {
        return _fullName;
    }

    public ChangeableProperty<String> get_avatar() {
        return _avatar;
    }

    public ChangeableProperty<String> get_email() {
        return _email;
    }

    public ChangeableProperty<Date> get_birthday() {
        return _birthday;
    }

    public ChangeableProperty<String> get_role() {
        return _role;
    }

    public ChangeableProperty<Integer> get_point() {
        return _point;
    }
}
