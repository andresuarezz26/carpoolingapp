package com.angular.gerardosuarez.carpoolingapp.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;

import com.angular.gerardosuarez.carpoolingapp.data.preference.map.MapPreference;

import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;

public final class StringUtils {

    private static final String OUTPUT_CHARSET = "UTF-8";
    private static final String ERROR_ENCODING_STRING = "error encoding String";
    private static final String ERROR_DECODING_STRING = "error decoding String";
    public static final String EMPTY_STRING = "";
    private static final String ZERO_STRING = "0";
    private static final int INT_TEN = 10;
    public static final String SLASH = "/";
    private static final String TODAY_STRING = "Hoy";
    private static final String FROM = "Inicio:";
    private static final String TO = "Destino:";

    private StringUtils() {

    }

    @NonNull
    public static String changeNullByEmpty(@Nullable String text) {
        return text == null ? EMPTY_STRING : text;
    }

    public static String buildRoute(String community, String fromOrTo, String date, String hour) {
        return fromOrTo + "-" + community + SLASH + date + SLASH + hour + SLASH;
    }

    public static String addZeroToStart(int number) {
        String formatNumber;
        if (number < INT_TEN) {
            formatNumber = ZERO_STRING + number;
        } else {
            formatNumber = number + EMPTY_STRING;
        }
        return formatNumber;
    }


    public static String encodeString(String string) {
        if (TextUtils.isEmpty(string)) {
            return string;
        }

        try {
            byte[] data = string.getBytes(OUTPUT_CHARSET);
            return Base64.encodeToString(data, Base64.DEFAULT);

        } catch (Exception e) {
            Timber.e(ERROR_ENCODING_STRING, e.getMessage());
        }
        return string;
    }

    public static String decodeString(String string) {
        if (TextUtils.isEmpty(string)) {
            return string;
        }
        String stringExtra = string;
        try {
            byte[] data = Base64.decode(string, Base64.DEFAULT);
            stringExtra = new String(data, OUTPUT_CHARSET);
        } catch (Exception e) {
            Timber.e(ERROR_DECODING_STRING, e.getMessage());
        }
        return stringExtra;
    }


    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty() || EMPTY_STRING.equals(string);
    }

    public static String formatDate(@Nullable String date) {
        if (date != null && date.length() != 9) return EMPTY_STRING;
        if (TextUtils.isEmpty(date)) return EMPTY_STRING;
        return date.substring(0, 2) +
                SLASH +
                date.substring(3, 5) +
                SLASH +
                date.substring(5, date.length());
    }

    public static String formatDateWithTodayLogic(@NonNull String dateString) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTimeInMillis(date.getTime());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        month++;
        String dayString = addZeroToStart(day);
        String monthString = addZeroToStart(month);
        String finalDate = dayString + monthString + year;
        if (dateString.equalsIgnoreCase(finalDate)) {
            return TODAY_STRING;
        } else {

            return formatString(dateString);
        }
    }

    private static String formatString(@NonNull String date) {
        if (date.length() != 8) return EMPTY_STRING;
        if (StringUtils.isEmpty(date)) return EMPTY_STRING;
        return date.substring(0, 2) +
                SLASH +
                date.substring(2, 4);
    }

    public static String formatHour(@Nullable String hour) {
        if (TextUtils.isEmpty(hour)) return EMPTY_STRING;
        return hour.substring(0, 2) +
                ":" +
                hour.substring(2, hour.length());
    }

    public static String getFromOrToFormattedText(@Nullable String fromOrTo, @Nullable String community) {
        if (TextUtils.isEmpty(fromOrTo) || TextUtils.isEmpty(community)) return EMPTY_STRING;
        else {
            if (MapPreference.FROM.equalsIgnoreCase(fromOrTo)) {
                return FROM + " \n" + community;
            } else {
                return TO + " \n" + community;
            }
        }
    }

    public static String removeNonPrintableCharacters(@NonNull String input) {
        return input.replaceAll("\\p{C}", "?");
    }
}
