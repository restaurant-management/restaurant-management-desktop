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

    public String toDisplayString() {
        switch (_value) {
            case "created":
                return "Chưa thanh toán";
            case "paid":
                return "Đã thanh toán";
            case "preparing":
                return "Đang chuẩn bị";
            case "prepare-done":
                return "Chuẩn bị xong";
            case "delivering":
                return "Đang mang ra bàn";
            case "shipping":
                return "Đang ship";
            case "complete":
                return "Hoàn thành";
            default:
                return "Lỗi";
        }
    }
}
