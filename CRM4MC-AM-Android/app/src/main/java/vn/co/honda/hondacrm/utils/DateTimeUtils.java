package vn.co.honda.hondacrm.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    public static String convertDisplayDayMonth(final int dayMonth) {
        if (dayMonth < 10) {
            StringBuilder temp = new StringBuilder(Constants.ZERO_STRING);
            return String.valueOf(temp.append(dayMonth));
        }
        return String.valueOf(dayMonth);
    }

    public static String convertDisplayDateTime(final int dayOfMonth, final int month, final int year) {
        return convertDisplayDayMonth(dayOfMonth) + Constants.SPLIT_DATE + convertDisplayDayMonth(month)
                + Constants.SPLIT_DATE + year;
    }

    public static String convertToSendServer(String date) {
        if (date == null || TextUtils.isEmpty(date)) {
            return Constants.EMPTY;
        }

        String[] items1;
        String d1;
        String m1;
        String y1;
        if (date.contains(Constants.SPLIT_DATE)) {
            items1 = date.split(Constants.SPLIT_DATE);
            d1 = items1[0];
            m1 = items1[1];
            y1 = items1[2];
        } else {
            items1 = date.split("-");
            y1 = items1[0];
            m1 = items1[1];
            d1 = items1[2];
        }
        int day = Integer.parseInt(d1);
        int month = Integer.parseInt(m1);
        int year = Integer.parseInt(y1);
        return year + "-" + convertDisplayDayMonth(month) + "-" + convertDisplayDayMonth(day);
    }

    public static String convertToDisplay(String date) {
        if (date == null || TextUtils.isEmpty(date)) {
            return Constants.EMPTY;
        }

        String[] items1;
        String dayOfMonth;
        String m1;
        String y1;
        if (date.contains(Constants.SPLIT_DATE)) {
            items1 = date.split(Constants.SPLIT_DATE);
            dayOfMonth = items1[0];
            m1 = items1[1];
            y1 = items1[2];
        } else {
            items1 = date.split("-");
            y1 = items1[0];
            m1 = items1[1];
            dayOfMonth = items1[2];
        }
        int day = Integer.parseInt(dayOfMonth);
        int month = Integer.parseInt(m1);
        int year = Integer.parseInt(y1);
        return convertDisplayDayMonth(day) + Constants.SPLIT_DATE + convertDisplayDayMonth(month)
                + Constants.SPLIT_DATE + year;
    }


    public static boolean validateBirthdate(String date) {
        Date currentTime = Calendar.getInstance().getTime();
        String[] itemsDate = date.split(Constants.SPLIT_DATE);
        if (itemsDate.length == 3) {
            String d1 = itemsDate[0];
            String m1 = itemsDate[1];
            String y1 = itemsDate[2];
            int y = Integer.parseInt(d1);
            int m = Integer.parseInt(m1);
            int d = Integer.parseInt(y1);
            int month = currentTime.getMonth() + 1;
            if ((currentTime.getYear() + 1900) < y) {
                return false;
            } else if ((currentTime.getYear() + 1900) == y && month < m) {
                return false;
            } else if ((currentTime.getYear() + 1900) == y && (month == m) && currentTime.getDate() < d) {
                return false;
            }
        }
        return true;

    }

    public static boolean isValidateBirthDate(String validDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = sdf.parse(validDate);
        if (System.currentTimeMillis() >= strDate.getTime()) {
            return true;
        }
        return false;
    }
}
