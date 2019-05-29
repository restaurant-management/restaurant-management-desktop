package model;

import model.enums.BillStatus;
import model.exceptions.IsNotABillStatusException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BillModel {
    private int _billId;
    private LocalDateTime _day;
    private BillStatus _status;
    private UserModel _user;
    private String _managerUsername;
    private ArrayList<BillDetailModel> _billDetails;

    public BillModel(JSONObject jsonObject) throws IsNotABillStatusException {
        _billId = jsonObject.getInt("billId");
        _day = LocalDateTime.parse(jsonObject.getString("day"), DateTimeFormatter.ISO_DATE_TIME);
        _status = BillStatus.get(jsonObject.getString("status"));
        _user = new UserModel(jsonObject.getJSONObject("user"));
        try {
            _managerUsername = jsonObject.getString("managerUsername");
        } catch (Exception e) {
            _managerUsername = null;
        }
        JSONArray jsonBillDetails = jsonObject.getJSONArray("billDetails");
        _billDetails = new ArrayList<>();
        for (Object object : jsonBillDetails) {
            try {
                _billDetails.add(new BillDetailModel((JSONObject)object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    BillModel(BillModel bill) {
        _billDetails = bill._billDetails;
        _managerUsername = bill._managerUsername;
        _user = bill._user;
        _status = bill._status;
        _day = bill._day;
        _billId = bill._billId;
    }

    public int get_billId() {
        return _billId;
    }

    public LocalDateTime get_day() {
        return _day;
    }

    public void set_day(LocalDateTime _day) {
        this._day = _day;
    }

    public BillStatus get_status() {
        return _status;
    }

    public void set_status(BillStatus _status) {
        this._status = _status;
    }

    public UserModel get_user() {
        return _user;
    }

    public String get_managerUsername() {
        return _managerUsername;
    }

    public void set_managerUsername(String _managerUsername) {
        this._managerUsername = _managerUsername;
    }

    public ArrayList<BillDetailModel> get_billDetails() {
        return _billDetails;
    }

    public void set_billDetails(ArrayList<BillDetailModel> _billDetails) {
        this._billDetails = _billDetails;
    }
}
