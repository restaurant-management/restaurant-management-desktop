package model;

import model.base.ChangeableProperty;
import model.enums.Permission;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoleModel {
    private int _roleId;
    private ChangeableProperty<String> _slug;
    private String _name;
    private String _description;
    private ArrayList<Permission> _permissions;

    public RoleModel(JSONObject jsonObject) {
        _roleId = jsonObject.getInt("roleId");
        _slug = new ChangeableProperty<>(jsonObject.getString("slug"));
        _name = jsonObject.getString("name");
        try {
            _description = jsonObject.getString("description");
        } catch (Exception e) {
            _description = null;
        }
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

    public RoleModel(RoleModel role) {
        _permissions = role._permissions;
        _description = role._description;
        _name = role._name;
        _slug = role._slug;
        _roleId = role._roleId;
    }

    public RoleModel(String slug, String name, String description, ArrayList<Permission> permissions) {
        _name = name;
        _slug = new ChangeableProperty<>(slug);
        _description = description;
        _permissions = permissions;
    }

    public int get_roleId() {
        return _roleId;
    }

    public String get_slug() {
        return _slug.get_value();
    }

    public void set_slug(String _slug) {
        this._slug.set_value(_slug);
    }

    public String get_baseSlug() {
        return _slug.get_base();
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public ArrayList<Permission> get_permissions() {
        return _permissions;
    }

    public void set_permissions(ArrayList<Permission> _permissions) {
        this._permissions = _permissions;
    }

    @Override
    public String toString() {
        return _name;
    }
}
