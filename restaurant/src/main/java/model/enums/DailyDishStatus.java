package model.enums;

import model.exceptions.IsNotADailyDishStatusException;

public enum DailyDishStatus {
    IN_STOCK("in-stock"),
    OUT_OF_STOCK("out-of-stock");


    private String _value;

    DailyDishStatus(String text) {
        _value = text;
    }

    public static DailyDishStatus get(String text) throws IsNotADailyDishStatusException {
        for (DailyDishStatus dailyDishStatus : DailyDishStatus.values()) {
            if (text.equals(dailyDishStatus.toString())) return dailyDishStatus;
        }
        throw new IsNotADailyDishStatusException();
    }

    @Override
    public String toString() {
        return _value;
    }

    public String toDisplayString() {
        switch (_value) {
            case "in-stock":
                return "Còn hàng";
            case "out-of-stock":
                return "Hết hàng";
            default:
                return "Lỗi";
        }
    }
}
