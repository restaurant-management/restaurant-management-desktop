package model.enums;

import model.exceptions.IsNotADaySessionException;

public enum DaySession {
    NONE("none"),
    MORNING("morning"),
    NOON("noon"),
    AFTERNOON("afternoon"),
    EVENING("evening");


    private String _value;

    DaySession(String text) {
        _value = text;
    }

    public static DaySession get(String text) throws IsNotADaySessionException {
        for (DaySession permission : DaySession.values()) {
            if (text.equals(permission.toString())) return permission;
        }
        throw new IsNotADaySessionException();
    }

    @Override
    public String toString() {
        return _value;
    }
}
