package model;

import org.json.JSONObject;

import java.util.ArrayList;

public class DishModel {
    private int _dishId;
    private String _name;
    private String _description;
    private ArrayList<String> _images;
    private int _defaultPrice;

    public DishModel(JSONObject jsonObject) {
        _dishId = jsonObject.getInt("dishId");
        _name = jsonObject.getString("name");
        _description = jsonObject.getString("description");
//        try {
//            _images = jsonObject.
//        } catch ()
        _defaultPrice = jsonObject.getInt("defaultPrice");
    }

    public DishModel(DishModel dishModel) {
        _dishId = dishModel._dishId;
        _name = dishModel._name;
        _images = dishModel._images;
        _defaultPrice = dishModel._defaultPrice;
        _description = dishModel._description;
    }

    public DishModel(int _dishId, String _name, String _description, ArrayList<String> _images, int _defaultPrice) {
        this._dishId = _dishId;
        this._name = _name;
        this._description = _description;
        this._images = _images;
        this._defaultPrice = _defaultPrice;
    }

    public int get_dishId() {
        return _dishId;
    }

    public String get_description() {
        return _description;
    }

    public int get_defaultPrice() {
        return _defaultPrice;
    }

    public String get_name() {
        return _name;
    }

    public ArrayList<String> get_images() {
        return _images;
    }

    public void set_defaultPrice(int _defaultPrice) {
        this._defaultPrice = _defaultPrice;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public void set_dishId(int _dishId) {
        this._dishId = _dishId;
    }

    public void set_images(ArrayList<String> _images) {
        this._images = _images;
    }

    public void set_name(String _name) {
        this._name = _name;
    }
}
