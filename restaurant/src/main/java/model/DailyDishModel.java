package model;

import org.json.JSONObject;

import java.util.Date;

public class DailyDishModel {
    private Date _day;
    private String _session;
    private int _dishId;
    private String _status;
    private int _price;
    private DishModel _dish;

    public DailyDishModel(DailyDishModel dailyDish) {
        this._day = dailyDish._day;
        this._session = dailyDish._session;
        this._dishId = dailyDish._dishId;
        this._status = dailyDish._status;
        this._price = dailyDish._price;
        this._dish = dailyDish._dish;
    }

    public DailyDishModel(JSONObject jsonObject) {
        this._day = (Date) jsonObject.get("day");
        this._session = jsonObject.getString("session");
        this._dishId = jsonObject.getInt("dishId");
        this._status = jsonObject.getString("status");
        this._price = jsonObject.getInt("price");
        this._dish = (DishModel) jsonObject.get("dish");
    }

    public DailyDishModel(Date _day, String _session, int _dishId, String _status, int _price, DishModel _dish) {
        this._day = _day;
        this._session = _session;
        this._dishId = _dishId;
        this._status = _status;
        this._price = _price;
        this._dish = _dish;
    }

    public Date get_day() {
        return _day;
    }

    public void set_day(Date _day) {
        this._day = _day;
    }

    public String get_session() {
        return _session;
    }

    public void set_session(String _session) {
        this._session = _session;
    }

    public int get_dishId() {
        return _dishId;
    }

    public void set_dishId(int _dishId) {
        this._dishId = _dishId;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public int get_price() {
        return _price;
    }

    public void set_price(int _price) {
        this._price = _price;
    }

    public DishModel get_dish() {
        return _dish;
    }

    public void set_dish(DishModel _dish) {
        this._dish = _dish;
    }
}
