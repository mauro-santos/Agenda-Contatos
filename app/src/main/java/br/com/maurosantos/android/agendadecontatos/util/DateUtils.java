package br.com.maurosantos.android.agendadecontatos.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by maurosantos on 31/10/2016.
 */

public class DateUtils {
    public static Date getDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTime();
    }

    public static String dateToString(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        return format.format(calendar.getTime());
    }

    public static String dateToString(Date date) {
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        return format.format(date);
    }
}
