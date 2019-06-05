package util;

import com.google.firebase.database.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Date increase(@NotNull Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    public static Date decrease(@NotNull Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}
