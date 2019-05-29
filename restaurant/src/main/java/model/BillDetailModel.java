package model;

import org.json.JSONObject;

public class BillDetailModel {
    private int _billId;
    private int _quantity;
    private Integer _price;
    private DishModel _dish;

    BillDetailModel(JSONObject jsonObject) {
        _billId = jsonObject.getInt("billId");
        _quantity = jsonObject.getInt("quantity");
        try {
            _price = jsonObject.getInt("price");
        } catch (Exception e) {
            _price = null;
        }
        _dish = new DishModel(jsonObject.getJSONObject("dish"));
    }

    BillDetailModel(BillDetailModel billDetailModel) {
        _billId = billDetailModel._billId;
        _quantity = billDetailModel._quantity;
        _price = billDetailModel._price;
        _dish = billDetailModel._dish;
    }

    public int get_billId() {
        return _billId;
    }

    public int get_quantity() {
        return _quantity;
    }

    public Integer get_price() {
        return _price;
    }

    public DishModel get_dish() {
        return _dish;
    }
}
