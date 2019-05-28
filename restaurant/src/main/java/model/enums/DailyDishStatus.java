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
        for (DailyDishStatus permission : DailyDishStatus.values()) {
            if (text.equals(permission.toString())) return permission;
        }
        throw new IsNotADailyDishStatusException();
    }

    @Override
    public String toString() {
        return _value;
    }
}
