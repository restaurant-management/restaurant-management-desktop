package model.enums;

import model.exceptions.IsNotABillStatusException;

public enum BillStatus {
    CREATED("created"),
    PAID("paid"),
    PREPARING("preparing"),
    PREPARE_DONE("prepare-done"),
    DELIVERING("delivering"),
    SHIPPING("shipping"),
    COMPLETE("complete");


    private String _value;

    BillStatus(String text) {
        _value = text;
    }

    public static BillStatus get(String text) throws IsNotABillStatusException {
        for (BillStatus billStatus : BillStatus.values()) {
            if (text.equals(billStatus.toString())) return billStatus;
        }
        throw new IsNotABillStatusException();
    }

    @Override
    public String toString() {
        return _value;
    }
}
