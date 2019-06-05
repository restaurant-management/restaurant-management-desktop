package model;

import org.json.JSONArray;
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
        try {
            _description = jsonObject.getString("description");
        } catch (Exception e) {
            _description = null;
        }
        try {
            JSONArray jsonImages = jsonObject.getJSONArray("images");
            _images = new ArrayList<>();
            for (Object object : jsonImages) {
                _images.add((String) object);
            }
        } catch (Exception e) {
            _images = null;
        }
        _defaultPrice = jsonObject.getInt("defaultPrice");
    }

    DishModel(DishModel dish) {
        _dishId = dish._dishId;
        _defaultPrice = dish._defaultPrice;
        _description = dish._description;
        _images = dish._images;
        _name = dish._name;
    }

    public DishModel(String name, String description, int defaultPrice, ArrayList<String> images) {
        _name = name;
        _description = description;
        _defaultPrice = defaultPrice;
        _images = images;
    }

    public int get_dishId() {
        return _dishId;
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

    public ArrayList<String> get_images() {
        return _images;
    }

    public void set_images(ArrayList<String> _images) {
        this._images = _images;
    }

    public int get_defaultPrice() {
        return _defaultPrice;
    }

    public void set_defaultPrice(int _defaultPrice) {
        this._defaultPrice = _defaultPrice;
    }
}
