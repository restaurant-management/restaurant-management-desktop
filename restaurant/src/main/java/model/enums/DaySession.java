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
        for (DaySession daySession : DaySession.values()) {
            if (text.equals(daySession.toString())) return daySession;
        }
        throw new IsNotADaySessionException();
    }

    @Override
    public String toString() {
        return _value;
    }

    public String toDisplayString() {
        switch (_value) {
            case "morning":
                return "Buổi sáng";
            case "noon":
                return "Buổi trưa";
            case "afternoon":
                return "Buổi chiều";
            case "evening":
                return "Buổi tối";
            case "none":
                return "Cả ngày";
            default:
                return "Lỗi";
        }
    }
}
