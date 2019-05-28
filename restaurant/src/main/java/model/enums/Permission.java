package model.enums;

import model.exceptions.IsNotAPermissionException;

public enum Permission {
    ROLE_MANAGEMENT("role-management"),
    USER_MANAGEMENT("user-management"),
    BILL_MANAGEMENT("bill-management"),
    CREATE_BILL("create-bill"),
    UPDATE_PREPARING_BILL_STATUS("update-preparing-bill-status"),
    UPDATE_PAID_BILL_STATUS("update-paid-bill-status"),
    UPDATE_PREPARE_DONE_BILL_STATUS("update-prepare-done-bill-status"),
    UPDATE_DELIVERING_BILL_STATUS("update-delivering-bill-status"),
    UPDATE_SHIPPING_BILL_STATUS("update-shipping-bill-status"),
    UPDATE_COMPLETE_BILL_STATUS("update-Complete-bill-status"),
    DISH_MANAGEMENT("dish-management"),
    DAILY_DISH_MANAGEMENT("daily-dish-management");


    private String _value;

    Permission(String permissionString) {
        _value = permissionString;
    }

    @Override
    public String toString() {
        return _value;
    }

    public boolean isPermission(String text) {
        for (Permission permission : Permission.values()) {
            if (text.equals(permission.toString())) return true;
        }
        return false;
    }

    public static Permission get(String text) throws IsNotAPermissionException {
        for (Permission permission : Permission.values()) {
            if (text.equals(permission.toString())) return permission;
        }
        throw new IsNotAPermissionException();
    }
}
