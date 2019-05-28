package model;

import model.enums.DailyDishStatus;
import model.enums.DaySession;
import model.exceptions.IsNotADailyDishStatusException;
import model.exceptions.IsNotADaySessionException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyDishModel {
    private Date _day;
    private DaySession _session;
    private DailyDishStatus _status;
    private int _price;
    private DishModel _dish;

    public DailyDishModel(JSONObject jsonObject) throws ParseException, IsNotADaySessionException, IsNotADailyDishStatusException {
        _day = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("day"));
        _session = DaySession.get(jsonObject.getString("session"));
        _price = jsonObject.getInt("price");
        _status = DailyDishStatus.get(jsonObject.getString("status"));
        _dish = new DishModel(jsonObject.getJSONObject("dish"));
    }

    public DailyDishModel(DailyDishModel dailyDish) {
        _dish = dailyDish._dish;
        _status = dailyDish._status;
        _price = dailyDish._price;
        _session = dailyDish._session;
        _day = dailyDish._day;
    }

    public Date get_day() {
        return _day;
    }

    public DaySession get_session() {
        return _session;
    }

    public DailyDishStatus get_status() {
        return _status;
    }

    public void set_status(DailyDishStatus _status) {
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
}